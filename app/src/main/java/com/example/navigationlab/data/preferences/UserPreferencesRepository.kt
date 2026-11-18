package com.example.navigationlab.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferencesRepository(
    private val context: Context
) {
    private val lastSearchQueryKey = stringPreferencesKey("last_search_query")
    val lastSearchQuery: Flow<String> =
        context.userPreferencesDataStore.data.map { preferences ->
            preferences[lastSearchQueryKey] ?: ""
        }
    suspend fun saveLastSearchQuery(value: String) {
        context.userPreferencesDataStore.edit { preferences ->
            preferences[lastSearchQueryKey] = value
        }
    }
}