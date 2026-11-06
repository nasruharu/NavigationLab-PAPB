package com.example.navigationlab.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp

@Composable
fun AboutScreen(
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("About", style = MaterialTheme.typography.headlineMedium)
        Text(text = "Tentang aplikasi praktikum.")
        Button(onClick = onBack) { Text("Kembali") }
    }
}