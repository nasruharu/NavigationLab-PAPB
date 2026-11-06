package com.example.navigationlab.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    onOpenDetail: (Int) -> Unit,
    onOpenProfile: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Home", style = MaterialTheme.typography.headlineMedium)
        Button(onClick = { onOpenDetail(101) }) {
            Text("Buka Detail Mahasiswa 101")
        }
        OutlinedButton(onClick = onOpenProfile) {
            Text("Buka Profile")
        }
    }
}