package com.example.navigationlab.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.navigationlab.ui.home.HomeUiState

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onOpenDetail: (Int) -> Unit,
    onOpenProfile: () -> Unit,
    onOpenAbout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Home", style = MaterialTheme.typography.headlineMedium)
        Text("Daftar Mahasiswa")
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            uiState.students.forEach { student ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onOpenDetail(student.id) }
                ) {
                    Text(
                        "${student.id}\n${student.name}",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
        OutlinedButton(onClick = onOpenProfile) {
            Text("Buka Profile")
        }
        OutlinedButton(onClick = onOpenAbout) {
            Text("Buka About")
        }
    }
}