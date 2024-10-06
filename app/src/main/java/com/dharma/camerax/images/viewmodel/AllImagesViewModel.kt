package com.dharma.camerax.images.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dharma.camerax.images.repo.AllImagesRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AllImagesViewModel
    @Inject constructor(
        private val allImagesRepo: AllImagesRepo,
        val outputFileDirectory : File
    ) : ViewModel() {

    private val _capturedImages = MutableStateFlow<List<Uri>>(emptyList())
    val capturedImages: StateFlow<List<Uri>> = _capturedImages.asStateFlow()

    // pagination
    private var allImageFiles: List<Uri> = emptyList()
    private var currentPage = 0
    private val pageSize = 10  // Number of images to load per page

    // Function to load images from a directory
    fun loadImagesFromDirectory() {

        viewModelScope.launch(Dispatchers.IO) {
            allImageFiles = allImagesRepo.loadImagesFromDirectory(outputFileDirectory)

            // Reset pagination
            currentPage = 0
            _capturedImages.update { emptyList() }

            loadNextPage()
        }
    }

    fun loadNextPage() {
        if (currentPage * pageSize >= allImageFiles.size) return  // No more images to load

        val startIndex = currentPage * pageSize
        val endIndex = (startIndex + pageSize).coerceAtMost(allImageFiles.size)

        // Update the StateFlow with new images
        _capturedImages.update { currentImages ->
            currentImages + allImageFiles.subList(startIndex, endIndex)
        }

        // Move to the next page
        currentPage++
    }

    fun clearImages() {
        _capturedImages.update { emptyList() }
        currentPage = 0
    }
}