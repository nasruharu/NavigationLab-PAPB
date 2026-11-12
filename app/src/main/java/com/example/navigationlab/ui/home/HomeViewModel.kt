package com.example.navigationlab.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.navigationlab.data.model.Student
import com.example.navigationlab.data.repository.StudentRepository

class HomeViewModel(
    private val repository: StudentRepository
) : ViewModel() {
    var uiState by mutableStateOf(HomeUiState())
        private set

    private var submittedQuery = ""
    private var requestId = 0

    init { loadStudents() }

    fun onSearchQueryChange(query: String) {
        uiState = uiState.copy(searchQuery = query)
    }

    fun searchStudents() {
        fetchStudents(uiState.searchQuery.trim())
    }

    fun loadStudents() {
        fetchStudents("")
    }

    fun retry() {
        fetchStudents(submittedQuery)
    }

    private fun fetchStudents(query: String) {
        submittedQuery = query
        val currentRequest = ++requestId
        uiState = uiState.copy(isLoading = true, errorMessage = null)
        val callback: (Result<List<Student>>) -> Unit = { result ->
            if (currentRequest == requestId) {
                result
                    .onSuccess { students ->
                        uiState = uiState.copy(
                            students = students,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                    .onFailure { error ->
                        uiState = uiState.copy(
                            students = emptyList(),
                            isLoading = false,
                            errorMessage = error.message ?: "Gagal mengambil data"
                        )
                    }
            }
        }
        if (query.isBlank()) {
            repository.getStudents(callback)
        } else {
            repository.searchStudents(query, callback)
        }
    }

    companion object {
        fun factory(repository: StudentRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T =
                    HomeViewModel(repository) as T
            }
    }
}
