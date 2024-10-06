package com.dharma.camerax.settings.repo

import kotlinx.coroutines.flow.Flow

interface SettingsRepo {

    fun isDarkTheme() : Flow<Boolean>
    suspend fun setDarkTheme(darkTheme: Boolean)

    fun isNotificationsEnabled() : Flow<Boolean>
    suspend fun setNotificationsEnabled(notificationsEnabled: Boolean)

}