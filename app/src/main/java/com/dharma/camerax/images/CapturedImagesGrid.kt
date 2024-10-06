package com.dharma.camerax.images

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

@Composable
fun CapturedImagesGrid(
    capturedImages: List<Uri>
) {
    // Use LazyVerticalGrid for grid layout
    LazyVerticalGrid(
        columns = GridCells.Fixed(2), // Display 2 items per row
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(capturedImages) { uri ->
            Column(
                modifier = Modifier.padding(8.dp)
            ){
                Image(
                    painter = rememberAsyncImagePainter(uri),
                    contentDescription = "Captured Image",
                    modifier = Modifier
                        .size(150.dp)
                        .padding(8.dp),
                    contentScale = ContentScale.Crop
                )
                Text(text = "Zoom : 1f", modifier = Modifier.padding(4.dp))
            }
            
        }
    }
}