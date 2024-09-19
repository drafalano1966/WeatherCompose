package com.example.weathercompose.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SearchDataStore(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("previousSearch")
        private val PREVIOUS_SEARCH_KEY = stringPreferencesKey("previous_search")
    }

    val getSearchToken: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[PREVIOUS_SEARCH_KEY] ?: "Bronx"
    }
    suspend fun saveToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[PREVIOUS_SEARCH_KEY] = token
        }
    }
}