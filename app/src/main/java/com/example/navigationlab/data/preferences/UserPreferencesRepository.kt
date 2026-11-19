package com.example.navigationlab.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import androidx.datastore.preferences.core.booleanPreferencesKey

class UserPreferencesRepository(
    private val context: Context
) {
    private val lastSearchQueryKey = stringPreferencesKey("last_search_query")
    private val sortAscendingKey = booleanPreferencesKey("sort_ascending")

    val lastSearchQuery: Flow<String> =
        context.userPreferencesDataStore.data.map { preferences ->
            preferences[lastSearchQueryKey] ?: ""
        }
    suspend fun saveLastSearchQuery(value: String) {
        context.userPreferencesDataStore.edit { preferences ->
            preferences[lastSearchQueryKey] = value
        }
    }
    val sortAscending: Flow<Boolean> =
        context.userPreferencesDataStore.data.map { preferences ->
            preferences[sortAscendingKey] ?: true
        }

    suspend fun saveSortAscending(value: Boolean) {
        context.userPreferencesDataStore.edit { preferences ->
            preferences[sortAscendingKey] = value
        }
    }
}