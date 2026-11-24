package com.example.navigationlab.ui.location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.navigationlab.data.location.LocationRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class LocationViewModel(
    private val repository: LocationRepository

) : ViewModel() {
    private val _uiState = MutableStateFlow<LocationUiState>(LocationUiState.Idle)
    val uiState: StateFlow<LocationUiState> = _uiState.asStateFlow()
    fun refreshLocation() {
        viewModelScope.launch {
            _uiState.value = LocationUiState.Loading
            _uiState.value = repository.getCurrentLocation()
                .fold(
                    onSuccess = { LocationUiState.Success(it) },
                    onFailure = { LocationUiState.Error(it.message ?: "Gagal mengambil lokasi") }
                )
        }
    }

}