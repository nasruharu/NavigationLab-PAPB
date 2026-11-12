package com.example.navigationlab.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import com.example.navigationlab.ui.home.HomeUiState

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onStudentClick: (Int) -> Unit,
    onRetry: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onSearch: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().safeDrawingPadding().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Pencarian Profil", style = MaterialTheme.typography.headlineMedium)
        OutlinedTextField(
            value = uiState.searchQuery,
            onValueChange = onSearchQueryChange,
            label = { Text("Kata pencarian") },
            placeholder = { Text("Contoh: John") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Button(onClick = onSearch, enabled = !uiState.isLoading) {
            Text("Cari")
        }
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            uiState.errorMessage != null -> {
                Text(uiState.errorMessage)
                Button(onClick = onRetry) { Text("Coba Lagi") }
            }
            uiState.students.isEmpty() -> Text("Profil tidak ditemukan")
            else -> {
                LazyColumn(modifier = Modifier.fillMaxWidth().weight(1f)) {
                    items(uiState.students, key = { it.id }) { student ->
                        TextButton(onClick = { onStudentClick(student.id) }) {
                            Text("${student.id} - ${student.name}")
                        }
                    }
                }
            }
        }
    }
}
