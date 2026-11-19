package com.example.navigationlab.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.semantics.*
import androidx.compose.ui.unit.dp
import com.example.navigationlab.ui.favorites.FavoritesUiState

@Composable
fun FavoritesScreen(
    uiState: FavoritesUiState,
    onSortAscendingChange: (Boolean) -> Unit,
    onStudentClick: (Int) -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Mahasiswa Favorit", style = MaterialTheme.typography.headlineMedium)
        if (uiState.isLoading) {
            CircularProgressIndicator()
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(if (uiState.sortAscending) "Urutan nama A–Z" else "Urutan nama Z–A")
                Switch(
                    checked = uiState.sortAscending,
                    onCheckedChange = onSortAscendingChange,
                    modifier = Modifier.semantics {
                        contentDescription = "Urutkan nama A–Z"
                    }
                )
            }

            if (uiState.students.isEmpty()) {
                Text("Belum ada mahasiswa favorit.")
            } else {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(uiState.students, key = { it.id }) { student ->
                        ListItem(
                            headlineContent = { Text(student.name) },
                            supportingContent = { Text(student.email) },
                            modifier = Modifier.clickable { onStudentClick(student.id) }
                        )
                    }
                }
            }
        }
        Button(onClick = onBack) { Text("Kembali") }
    }
}