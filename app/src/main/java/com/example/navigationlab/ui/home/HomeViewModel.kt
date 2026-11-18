package com.example.navigationlab.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.navigationlab.data.repository.StudentRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException
import com.example.navigationlab.data.preferences.UserPreferencesRepository
import kotlinx.coroutines.flow.first

class HomeViewModel(
    private val repository: StudentRepository,
    private val preferencesRepository: UserPreferencesRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    init { loadStudents() }

    init { observeSearch() }

    init {
        viewModelScope.launch {
            _searchQuery.value = preferencesRepository.lastSearchQuery.first()
        }
        viewModelScope.launch {
            searchQuery
                .debounce(500)
                .distinctUntilChanged()
                .collectLatest { value ->
                    preferencesRepository.saveLastSearchQuery(value)
                }
        }
    }

    fun loadStudents() {
        viewModelScope.launch {
            _uiState.value = HomeUiState(isLoading = true)
            try {
                val students = repository.getStudents()
                _uiState.value = HomeUiState(students = students)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = HomeUiState(
                    errorMessage = e.message ?: "Gagal mengambil data"
                )
            }
        }
    }

    @OptIn(FlowPreview::class)
    private fun observeSearch() {
        viewModelScope.launch {
            searchQuery
                .debounce(500)
                .distinctUntilChanged()
                .collectLatest { query ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = true,
                        errorMessage = null
                    )
                    try {
                        val data = if (query.isBlank()) {
                            repository.getStudents()
                        } else {
                            repository.searchStudents(query)
                        }
                        _uiState.value = HomeUiState(students = data)
                    } catch (e: CancellationException) {
                        throw e
                    } catch (e: Exception) {
                        _uiState.value = HomeUiState(
                            errorMessage = e.message ?: "Pencarian gagal"
                        )
                    }
                }
        }
    }

    fun retry() {
        val query = searchQuery.value
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            try {
                val data = if (query.isBlank()) {
                    repository.getStudents()
                } else {
                    repository.searchStudents(query)
                }
                _uiState.value = HomeUiState(students = data)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = HomeUiState(
                    errorMessage = e.message ?: "Request gagal"
                )
            }
        }
    }

    fun refresh() {
        val query = searchQuery.value

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            try {
                val data = if (query.isBlank()) {
                    repository.getStudents()
                } else {
                    repository.searchStudents(query)
                }
                _uiState.value = HomeUiState(
                    students = data
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = HomeUiState(
                    errorMessage = e.message ?: "Refresh gagal"
                )
            }
        }
    }

    companion object {
        fun factory(
            repository: StudentRepository,
            preferencesRepository: UserPreferencesRepository
        ): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return HomeViewModel(repository, preferencesRepository) as T
                }
            }
    }
}