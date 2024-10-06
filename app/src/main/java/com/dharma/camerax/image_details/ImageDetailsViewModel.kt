package com.dharma.camerax.image_details

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dharma.camerax.images.repo.AllImagesRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ImageDetailsViewModel @Inject constructor(
    private val imagesRepo: AllImagesRepo,
    private val outputDirectory: File
) : ViewModel() {

    private val _capturedImages = MutableStateFlow<List<Uri>>(emptyList())

    var allImagesCount = 0

    private val _currentIndex = MutableStateFlow(0)
    val currentIndex = _currentIndex.asStateFlow()

    private val _currentImage = MutableStateFlow<Uri?>(null)
    val currentImage = _currentImage.asStateFlow()

    fun displayImage(currentImage : Uri){
        _currentImage.value = currentImage
        viewModelScope.launch(Dispatchers.IO) {
            _capturedImages.value = imagesRepo.loadImagesFromDirectory(outputDirectory)
            _currentIndex.value = _capturedImages.value.indexOf(currentImage)
            allImagesCount = _capturedImages.value.size
        }
    }

    fun moveToPreviousImage(currentIndex: Int) {
        if(_currentIndex.value>0){
            _currentIndex.value = currentIndex - 1
        }else{
            _currentIndex.value = _capturedImages.value.lastIndex
        }
        updateCurrentImage()
    }

    fun moveToNextImage(currentIndex: Int) {
        if (_currentIndex.value < _capturedImages.value.lastIndex) {
            _currentIndex.value = currentIndex + 1
        } else {
            _currentIndex.value = 0
        }
        updateCurrentImage()
    }

    private fun updateCurrentImage() {
        _currentImage.value = _capturedImages.value.getOrNull(_currentIndex.value)
    }
}