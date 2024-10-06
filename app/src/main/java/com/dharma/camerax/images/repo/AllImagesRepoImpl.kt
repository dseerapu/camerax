package com.dharma.camerax.images.repo

import android.net.Uri
import java.io.File

class AllImagesRepoImpl : AllImagesRepo {

    override suspend fun loadImagesFromDirectory(directory: File): List<Uri> {
        val imageFiles = directory.listFiles { file ->
            file.extension.lowercase() in listOf("jpg", "jpeg")
        }?.map { file -> Uri.fromFile(file) } ?: emptyList()
        return imageFiles
    }
}