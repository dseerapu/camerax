package com.dharma.camerax

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.dharma.camerax.navigation.AppNavigation
import com.dharma.camerax.settings.repo.SettingsRepo
import com.dharma.camerax.ui.theme.CameraxTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var settingsRepo : SettingsRepo

    private lateinit var cameraExecutor: ExecutorService
    private lateinit var notificationPermissionLauncher: ActivityResultLauncher<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Register for the notification permission result
        notificationPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                lifecycleScope.launch {
                    settingsRepo.setNotificationsEnabled(true)
                }

            } else {
                lifecycleScope.launch {
                    settingsRepo.setNotificationsEnabled(false)
                }
            }
        }


        // Check and request notification permission
        requestNotificationPermission()

        cameraExecutor = Executors.newSingleThreadExecutor()

        enableEdgeToEdge()
        setContent {

            val darkTheme = settingsRepo.isDarkTheme().collectAsState(initial = isSystemInDarkTheme())

            CameraxTheme(darkTheme.value) {
                val navaController = rememberNavController()
                AppNavigation(
                    cameraExecutor = cameraExecutor,
                    navaController = navaController)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    private fun requestNotificationPermission() {
        // Check if the notification permission is granted
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {

                // Request the permission
                notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}

