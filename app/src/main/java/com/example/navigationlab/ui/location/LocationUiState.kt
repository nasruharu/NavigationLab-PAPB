package com.example.navigationlab.ui.location

import com.example.navigationlab.data.location.DeviceLocation

sealed interface LocationUiState {
    data object Idle : LocationUiState
    data object Loading : LocationUiState
    data class Success(val location: DeviceLocation) : LocationUiState
    data class Error(val message: String) : LocationUiState
    data object PermissionDenied : LocationUiState
}