package com.example.navigationlab.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.navigationlab.data.model.Student
import com.example.navigationlab.ui.home.HomeUiState

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onSearchQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onStudentClick: (Int) -> Unit,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .safeDrawingPadding(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Profil App", style = MaterialTheme.typography.headlineMedium)
        OutlinedTextField(
            value = uiState.searchQuery,
            onValueChange = onSearchQueryChange,
            label = { Text("Cari Profil") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Button(onClick = onSearch, enabled = !uiState.isLoading) {
            Text("Cari")
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text("Daftar Profil", style = MaterialTheme.typography.headlineSmall)
        when {
            uiState.isLoading -> {
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            uiState.errorMessage != null -> {
                Text(uiState.errorMessage)
                Button(onClick = onRetry) { Text("Coba Lagi") }
            }
            uiState.students.isEmpty() -> {
                Text("Profil tidak ditemukan.")
            }
            else -> {
                LazyColumn(modifier = Modifier.weight(1f)) {
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