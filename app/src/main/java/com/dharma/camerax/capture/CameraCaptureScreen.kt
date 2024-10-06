package com.dharma.camerax.capture

import android.content.Context
import android.net.Uri
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.dharma.camerax.CaptureImagesViewModel
import com.dharma.camerax.R
import com.dharma.camerax.capture.GenerateImagesWithZoomLevelsWorker.Companion.ORIGINAL_IMAGE_PATH
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutorService

@Composable
fun CameraCaptureScreen(
    executor: ExecutorService,
    navController: NavController,
    viewModel: CaptureImagesViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var camera: Camera? by remember { mutableStateOf(null) }

    // Create an ImageCapture use case
    val imageCapture = remember { ImageCapture.Builder().build() }
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val coroutineScope = rememberCoroutineScope()
    var isCapturingImage by remember { mutableStateOf(false) }

    Scaffold(
        content = { paddingValues ->

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {

                // Camera Preview occupying the full screen
                Box(modifier = Modifier.weight(1f)) {
                    AndroidView(
                        factory = { viewContext ->
                            val previewView = PreviewView(viewContext)
                            cameraProviderFuture.addListener({
                                val cameraProvider = cameraProviderFuture.get()

                                // Set up the Preview use case
                                val preview = Preview.Builder().build().also {
                                    it.setSurfaceProvider(previewView.surfaceProvider)
                                }

                                // Select the back camera as the default
                                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                                try {
                                    // Unbind all use cases before rebinding
                                    cameraProvider.unbindAll()

                                    // Bind the camera to the lifecycle and set the preview
                                    camera = cameraProvider.bindToLifecycle(
                                        lifecycleOwner,
                                        cameraSelector,
                                        preview,
                                        imageCapture
                                    )

                                } catch (exc: Exception) {
                                    exc.printStackTrace()
                                }
                            }, ContextCompat.getMainExecutor(viewContext))

                            previewView
                        },
                        modifier = Modifier.fillMaxSize()
                    )

                    // Close Icon in the top right corner
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(16.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_launcher_foreground), // Replace with your close icon
                            contentDescription = "Close",
                            tint = Color.White // Set icon color to white for visibility
                        )
                    }

                    // Capture Icon
                    IconButton(
                        onClick = {
                            isCapturingImage = true
                            captureImage(
                                outputDirectory = viewModel.outputDirectory,
                                camera = camera,
                                imageCapture = imageCapture,
                                executor = executor,
                                onImageCaptured = { uri ->
                                    // Adding new images
                                    viewModel.addCapturedImages(listOf(uri))

                                    // Generating images with zoom levels
                                    generateImagesWithZoomLevels(context, uri)

                                    // Navigate back to the main screen after capturing images
                                    coroutineScope.launch {
                                        navController.popBackStack()
                                    }
                                    isCapturingImage = false
                                }
                            )
                        },
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(16.dp) // Adjust padding as needed
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_camera), // Replace with your capture icon
                            contentDescription = "Capture",
                            Modifier.size(96.dp),
                            tint = Color.White // Set icon color to white for visibility
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Show loader when capturing the image
                if (isCapturingImage) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator() // Loader
                    }
                }
            }
        })
}

/**
 * Generating images with multiple zoom levels
 */
private fun generateImagesWithZoomLevels(context: Context, uri: Uri) {

    val workManager = WorkManager.getInstance(context)

    // Prepare input data for WorkManager
    val outputDirectoryPath = uri.path

    val inputData = Data.Builder()
        .putString(ORIGINAL_IMAGE_PATH, outputDirectoryPath)
        .build()

    // Create a work request for capturing images
    val workRequest = OneTimeWorkRequestBuilder<GenerateImagesWithZoomLevelsWorker>()
        .setInputData(inputData)
        .build()

    // Enqueue the work request
    workManager.enqueue(workRequest)
}