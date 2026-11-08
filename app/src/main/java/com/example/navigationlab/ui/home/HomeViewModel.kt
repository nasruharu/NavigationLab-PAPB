package com.example.navigationlab.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.navigationlab.data.repository.StudentRepository

class HomeViewModel(
    private val repository: StudentRepository
) : ViewModel() {
    var uiState by mutableStateOf(HomeUiState())
        private set
    init { loadStudents() }
    fun loadStudents() {
        uiState = uiState.copy(isLoading = true, errorMessage = null)
        repository.getStudents { result ->
            result
                .onSuccess { data ->
                    uiState = HomeUiState(students = data)
                }
                .onFailure { error ->
                    uiState = HomeUiState(
                        errorMessage = error.message ?: "Gagal mengambil data"
                    )
                }
        }
    }
}