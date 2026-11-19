package com.example.navigationlab.ui.favorites

import com.example.navigationlab.data.model.FavoriteStudent

data class FavoritesUiState(
    val students: List<FavoriteStudent> = emptyList(),
    val sortAscending: Boolean = true,
    val isLoading: Boolean = true
)