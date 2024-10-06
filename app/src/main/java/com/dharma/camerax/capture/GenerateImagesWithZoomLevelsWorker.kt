package com.dharma.camerax.capture

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.dharma.camerax.MainActivity
import com.dharma.camerax.notifications.isNotificationPermissionGranted
import com.dharma.camerax.notifications.sendNotification
import com.dharma.camerax.settings.repo.SettingsRepo
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

@HiltWorker
class GenerateImagesWithZoomLevelsWorker
        @AssistedInject constructor(
            @Assisted context: Context,
            @Assisted params: WorkerParameters,
            private val outputDirectory: File,
            private val settingsRepo: SettingsRepo
) : CoroutineWorker(context, params){

    override suspend fun doWork(): Result {
        val originalImagePath = inputData.getString(ORIGINAL_IMAGE_PATH) ?: return Result.failure()

        return try {
            // Create zoomed versions from the existing original image
            createZoomedVersions(File(originalImagePath), outputDirectory)

            if(isNotificationPermissionGranted(applicationContext)){
                sendNotification(applicationContext,MainActivity::class.java,
                    "All Images Saved", "Zoomed images have been saved successfully.")
            }

            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }

    private suspend fun createZoomedVersions(photoFile: File, outputDirectory: File) {
        withContext(Dispatchers.IO) {
            // Load the original image as a Bitmap
            val originalBitmap = BitmapFactory.decodeFile(photoFile.absolutePath)

            // Get the EXIF orientation of the image
            val exif = ExifInterface(photoFile.absolutePath)
            val rotationDegrees = exifToDegrees(exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL))

            // Correct the rotation of the original bitmap
            val correctedBitmap = rotateBitmap(originalBitmap, rotationDegrees)

            // Define zoom levels (e.g., 1.0x, 2.0x, 3.0x)
            val zoomLevels = listOf(1.5f, 2.0f, 2.5f, 3.0f)

            for (zoomLevel in zoomLevels) {
                // Create a zoomed version of the original image
                val zoomedBitmap = zoomImage(correctedBitmap, zoomLevel)

                // Save the zoomed version
                val zoomedFile = File(outputDirectory, "${System.currentTimeMillis()}_zoom_$zoomLevel.jpg")
                saveBitmapToFile(zoomedBitmap, zoomedFile)
            }
        }
    }

    private fun zoomImage(originalBitmap: Bitmap, zoomFactor: Float): Bitmap {
        val width = originalBitmap.width
        val height = originalBitmap.height

        // Calculate the center crop dimensions for the zoom
        val newWidth = (width / zoomFactor).toInt()
        val newHeight = (height / zoomFactor).toInt()

        // Crop and scale the original bitmap
        val matrix = Matrix()
        matrix.setScale(zoomFactor, zoomFactor)

        return Bitmap.createBitmap(
            originalBitmap,
            (width - newWidth) / 2,
            (height - newHeight) / 2,
            newWidth,
            newHeight,
            matrix,
            true
        )
    }

    private fun saveBitmapToFile(bitmap: Bitmap, file: File) {
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out) // Compress and save as JPEG
        }
        println("Zoomed image saved to: ${file.absolutePath}")
    }

    // Rotate the bitmap according to the EXIF orientation
    private fun rotateBitmap(bitmap: Bitmap, degrees: Float): Bitmap {
        if (degrees == 0f) return bitmap
        val matrix = Matrix()
        matrix.postRotate(degrees)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    // Convert EXIF orientation to degrees
    private fun exifToDegrees(exifOrientation: Int): Float {
        return when (exifOrientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90f
            ExifInterface.ORIENTATION_ROTATE_180 -> 180f
            ExifInterface.ORIENTATION_ROTATE_270 -> 270f
            else -> 0f
        }
    }

    companion object{
        const val ORIGINAL_IMAGE_PATH = "originalImagePath"
    }
}