package com.example.navigationlab.ui.location

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun LocationScreen(
    viewModel: LocationViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    fun getPermissionType(): String? {
        val fineGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val coarseGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        return when {
            fineGranted -> "Precise"
            coarseGranted -> "Approximate"
            else -> null
        }
    }

    var permissionType by remember {
        mutableStateOf(getPermissionType())
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        val coarse = result[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        val fine = result[Manifest.permission.ACCESS_FINE_LOCATION] == true
        permissionType = getPermissionType()
        if (coarse || fine) viewModel.refreshLocation()
        else viewModel.onPermissionDenied()
    }

    fun requestLocation() {
        permissionType = getPermissionType()

        if (permissionType != null) { viewModel.refreshLocation() }
        else {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            )
        }
    }

    val dateFormatter = remember {
        SimpleDateFormat(
            "dd MMM yyyy, HH:mm:ss",
            Locale.getDefault()
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .safeDrawingPadding(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Lokasi Saya", style = MaterialTheme.typography.headlineMedium)
        Text("Jenis akses: ${permissionType ?: "Belum diberikan"}")
        when (val state = uiState) {
            LocationUiState.Idle -> Text("Lokasi belum diminta")
            LocationUiState.Loading -> {
                CircularProgressIndicator()
                Text("Sedang mengambil lokasi...")
            }
            is LocationUiState.Success -> {
                Text("Latitude: ${state.location.latitude}")
                Text("Longitude: ${state.location.longitude}")
                Text("Accuracy: ${state.location.accuracy?.let { "$it m" } ?: "Tidak tersedia"}")
                Text("Terakhir diperbarui: ${dateFormatter.format(Date(state.location.updatedAt))}")
            }
            LocationUiState.PermissionDenied -> {
                Text(text = "Izin lokasi ditolak. Berikan izin lokasi agar fitur ini dapat digunakan.",
                    color = MaterialTheme.colorScheme.error
                )
            }
            is LocationUiState.Error -> {
                Text(text = state.message, color = MaterialTheme.colorScheme.error)
            }
        }
        Button(
            onClick = { requestLocation() },
            enabled = uiState != LocationUiState.Loading
        ) {
            Text(
                when (uiState) {
                    LocationUiState.PermissionDenied -> "Minta Izin Lagi"
                    is LocationUiState.Success -> "Perbarui Lokasi"
                    is LocationUiState.Error -> "Coba Lagi"
                    LocationUiState.Loading -> "Mengambil Lokasi..."
                    LocationUiState.Idle -> "Ambil Lokasi Saya"
                }
            )
        }
        OutlinedButton(onClick = onBack) { Text("Kembali") }
    }
}