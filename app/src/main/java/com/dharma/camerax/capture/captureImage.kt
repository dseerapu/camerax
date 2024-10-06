package com.dharma.camerax.capture

import android.net.Uri
import androidx.camera.core.Camera
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.concurrent.ExecutorService
fun captureImage(
    outputDirectory: File,
    camera: Camera?,
    imageCapture : ImageCapture,
    executor: ExecutorService,
    onImageCaptured: (Uri) -> Unit
) {
        // Set the zoom level for the camera
        camera?.cameraControl?.setZoomRatio(1f)

        // Create a file for saving the image
        val photoFile = File(outputDirectory, "${System.currentTimeMillis()}_1.0.jpg")
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        // Capture the image
         imageCapture.takePicture(outputOptions, executor, object : ImageCapture.OnImageSavedCallback {
             override fun onError(exc: ImageCaptureException) {
                 exc.printStackTrace()
             }

             override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                 val savedUri = Uri.fromFile(photoFile)
                 onImageCaptured(savedUri)
             }
         })

 }