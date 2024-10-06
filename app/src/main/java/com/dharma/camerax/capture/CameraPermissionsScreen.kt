package com.dharma.camerax.capture

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.dharma.camerax.CaptureImagesViewModel
import java.util.concurrent.ExecutorService

@Composable
fun CapturePermissionsScreen(
    cameraExecutor: ExecutorService,
    navController: NavHostController,
    viewModel: CaptureImagesViewModel = hiltViewModel()
) {
    var hasCameraPermission by remember { mutableStateOf(false) }
    var permissionDenied by remember { mutableStateOf(false) } // Track if permission was denied

    // Launcher to request camera permission
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                hasCameraPermission = true
                permissionDenied = false
            } else {
                permissionDenied = true // Set flag if permission denied
            }
        }
    )

    LaunchedEffect(Unit) {
        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
    }

    if (hasCameraPermission) {

        // If permission is granted, navigate to the camera capture screen
        CameraCaptureScreen(
            executor = cameraExecutor,
            navController = navController,
            viewModel = viewModel
        )
    } else if (permissionDenied) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Camera permission is required to use this feature.")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                // Request camera permission again when button is clicked
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                navigateToSettings(navController)
            }) {
                Text("Request Camera Permission")
            }
        }
    }
}

private fun navigateToSettings(navController: NavHostController) {
    // Create an intent that opens the app settings
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.parse("package:${navController.context.packageName}")
    }
    navController.context.startActivity(intent)
}