package com.example.navigationlab.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp

@Composable
fun DetailScreen(
    studentId: Int,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Detail", style = MaterialTheme.typography.headlineMedium)
        Text("Student ID: $studentId")
        Button(onClick = onBack) { Text("Kembali") }
    }
}