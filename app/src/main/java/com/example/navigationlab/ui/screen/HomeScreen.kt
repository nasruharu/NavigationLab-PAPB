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
    onRetry: () -> Unit
) {
    when {
        uiState.isLoading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        uiState.errorMessage != null -> {
            Column(
                modifier = Modifier.fillMaxSize().padding(24.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(uiState.errorMessage)
                Button(onClick = onRetry) { Text("Coba Lagi") }
            }
        }
        else -> {
            LazyColumn {
                items(uiState.students) { student ->
                    TextButton(onClick = { onStudentClick(student.id) }) {
                        Text("${student.id} - ${student.name}")
                    }
                }
            }
        }
    }
}