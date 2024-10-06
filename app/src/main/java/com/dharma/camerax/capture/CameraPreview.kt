package com.dharma.camerax.capture

import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.common.util.concurrent.ListenableFuture

@Composable
fun CameraPreview(
    lifecycleOwner: LifecycleOwner,
    cameraProviderFuture: ListenableFuture<ProcessCameraProvider>,
    onCameraInitialized: (Camera?) -> Unit,
    modifier: Modifier,
) {

    // Camera Preview
    Box(modifier = modifier) {
        AndroidView(factory = { viewContext ->
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
                    val camera  = cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview
                    )
                    onCameraInitialized.invoke(camera)

                } catch (exc: Exception) {
                    onCameraInitialized.invoke(null)
                    exc.printStackTrace()
                }
            }, ContextCompat.getMainExecutor(viewContext))

            previewView
        })
    }

}