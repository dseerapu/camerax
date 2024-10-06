package com.dharma.camerax.di

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.dharma.camerax.R
import com.dharma.camerax.images.repo.AllImagesRepo
import com.dharma.camerax.images.repo.AllImagesRepoImpl
import com.dharma.camerax.settings.repo.SettingsRepo
import com.dharma.camerax.settings.repo.SettingsRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

// Create an extension property for DataStore
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun providesAllImagesRepo()  : AllImagesRepo = AllImagesRepoImpl()

    @Provides
    fun provideImageStorageFilePath(
        application: Application
    ) : File{

        val mediaDir = application.externalMediaDirs.firstOrNull()?.let {
            File(it, application.resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return mediaDir ?: application.filesDir
    }

    @Provides
    fun provideExecutor(): ExecutorService {
        return Executors.newSingleThreadExecutor()
    }

    @Provides
    fun providesSettingsRepo(
        dataStore: DataStore<Preferences>
    ) : SettingsRepo = SettingsRepoImpl(dataStore)

    @Provides
    fun getDataStore( @ApplicationContext context: Context) = context.dataStore

}