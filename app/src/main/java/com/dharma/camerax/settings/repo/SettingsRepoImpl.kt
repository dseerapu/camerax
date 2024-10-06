package com.dharma.camerax.settings.repo

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsRepoImpl(private val dataStore: DataStore<Preferences>) : SettingsRepo{

    override fun isDarkTheme(): Flow<Boolean> {
       return dataStore.data
            .map { preferences ->
                preferences[THEME_PREFERENCE_KEY] ?: false // Default to false (light theme)
            }
    }

    override suspend fun setDarkTheme(darkTheme: Boolean) {
        dataStore.edit { preferences ->
            preferences[THEME_PREFERENCE_KEY] = darkTheme
        }
    }

    override fun isNotificationsEnabled(): Flow<Boolean> {
        return dataStore.data
            .map { preferences ->
                preferences[NOTIFICATION_PREFERENCE_KEY] ?: false
            }
    }

    override suspend fun setNotificationsEnabled(notificationsEnabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[NOTIFICATION_PREFERENCE_KEY] = notificationsEnabled
        }
    }

    companion object{
        private val THEME_PREFERENCE_KEY = booleanPreferencesKey("theme_preference_key")
        private val NOTIFICATION_PREFERENCE_KEY = booleanPreferencesKey("is_notifications_enabled")
    }
}
