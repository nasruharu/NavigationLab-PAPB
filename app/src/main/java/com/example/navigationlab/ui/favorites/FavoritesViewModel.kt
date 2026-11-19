package com.example.navigationlab.ui.favorites

import androidx.lifecycle.*
import com.example.navigationlab.data.model.FavoriteStudent
import com.example.navigationlab.data.preferences.UserPreferencesRepository
import com.example.navigationlab.data.repository.FavoriteRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class FavoritesViewModel(
    favoriteRepository: FavoriteRepository,
    private val preferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val byName = compareBy<FavoriteStudent, String>(
        String.CASE_INSENSITIVE_ORDER
    ) { it.name }.thenBy { it.id }

    val uiState: StateFlow<FavoritesUiState> = combine(
        favoriteRepository.observeFavorites(),
        preferencesRepository.sortAscending
    ) { favorites, sortAscending ->
        val order = if (sortAscending) byName else byName.reversed()
        FavoritesUiState(
            students = favorites.sortedWith(order),
            sortAscending = sortAscending,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = FavoritesUiState()
    )

    fun onSortAscendingChange(sortAscending: Boolean) {
        viewModelScope.launch {
            preferencesRepository.saveSortAscending(sortAscending)
        }
    }

    companion object {
        fun factory(
            favoriteRepository: FavoriteRepository,
            preferencesRepository: UserPreferencesRepository
        ): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return FavoritesViewModel(favoriteRepository, preferencesRepository) as T
                }
            }
    }
}