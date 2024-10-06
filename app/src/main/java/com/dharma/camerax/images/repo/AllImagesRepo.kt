package com.dharma.camerax.images.repo

import android.net.Uri
import java.io.File

interface AllImagesRepo {

    suspend fun loadImagesFromDirectory(directory : File) : List<Uri>
}