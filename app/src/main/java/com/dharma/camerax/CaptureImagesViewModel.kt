package com.dharma.camerax

import android.net.Uri
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.io.File
import javax.inject.Inject

@HiltViewModel
class CaptureImagesViewModel @Inject constructor(
    val outputDirectory: File) : ViewModel() {

    // Mutable StateFlow to store captured images
    private val _capturedImages = MutableStateFlow<List<Uri>>(emptyList())
    val capturedImages: StateFlow<List<Uri>> = _capturedImages.asStateFlow()

    // Function to add new images to the flow
    fun addCapturedImages(newImages: List<Uri>) {
        _capturedImages.update { currentImages ->
            currentImages + newImages
        }
    }

    // Function to reset the captured images
    fun clearImages() {
        _capturedImages.update { emptyList() }
    }
}