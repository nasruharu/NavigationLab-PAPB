package com.example.navigationlab.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import com.example.navigationlab.ui.detail.DetailUiState

@Composable
fun DetailScreen(
    uiState: DetailUiState,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Detail", style = MaterialTheme.typography.headlineMedium)

        when {
            uiState.errorMessage != null ->
                Text(uiState.errorMessage)
            uiState.student != null -> {
                val student = uiState.student
                Text("ID: ${student.id}")
                Text("Nama: ${student.name}")
                Text("Program: ${student.program}")
                Text("Angkatan: ${student.year}")
            }
        }

        Button(onClick = onBack) { Text("Kembali") }
    }
}