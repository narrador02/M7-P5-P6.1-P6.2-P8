package com.example.m07_p5.data

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "app_preferences")

class DataStoreManager(private val context: Context) {

    companion object {
        val USAGE_COUNT_KEY = intPreferencesKey("usage_count")
        val LAST_ACTION_KEY = stringPreferencesKey("last_action")
    }

    suspend fun saveUsageCount(count: Int) {
        context.dataStore.edit { preferences ->
            preferences[USAGE_COUNT_KEY] = count
        }
    }

    suspend fun saveLastAction(action: String) {
        context.dataStore.edit { preferences ->
            preferences[LAST_ACTION_KEY] = action
        }
    }

    val usageCount: Flow<Int> = context.dataStore.data
        .map { preferences -> preferences[USAGE_COUNT_KEY] ?: 0 }

    val lastAction: Flow<String> = context.dataStore.data
        .map { preferences -> preferences[LAST_ACTION_KEY] ?: "N/A" }
}