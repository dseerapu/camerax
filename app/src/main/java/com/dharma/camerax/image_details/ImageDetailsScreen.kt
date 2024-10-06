package com.dharma.camerax.image_details

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.dharma.camerax.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageDetailsScreen(
    navController: NavController,
    imageUri: Uri,
    viewModel: ImageDetailsViewModel = hiltViewModel()
) {

    LaunchedEffect(Unit) {
        viewModel.displayImage(imageUri)
    }

    val currentIndex by viewModel.currentIndex.collectAsState(initial = 0)
    val currentImage by viewModel.currentImage.collectAsState()

    // Extract zoom level from URI and display it
    val zoomLevel = currentImage.toString().split("/").last().split("_").last().split(".jpg").first()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.zoom_level)+ " : "+ zoomLevel) },
                navigationIcon ={
                    IconButton(onClick = {
                        navController.popBackStack()
                    }){
                        Icon(
                            Icons.Default.Close,
                            contentDescription = stringResource(R.string.close)
                        )
                    }
                })
        },
        content = { _ ->
            Box(
                contentAlignment = Alignment.Center
            ) {
                // Display image with pinch zoom and double-tap zoom functionality
                ZoomableImage(uri = currentImage)

                // Navigation buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Previous Button
                    IconButton(
                        onClick = {
                            viewModel.moveToPreviousImage(currentIndex)
                        },
                        enabled = currentIndex > 0,
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.primaryContainer, CircleShape)
                            .padding(8.dp)
                    ) {
                        Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Previous Image")
                    }

                    // Next Button
                    IconButton(
                        onClick = {
                            viewModel.moveToNextImage(currentIndex)
                        },
                        enabled = currentIndex < viewModel.allImagesCount,
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.primaryContainer, CircleShape)
                            .padding(8.dp)
                    ) {
                        Icon(Icons.AutoMirrored.Default.ArrowForward, contentDescription = "Next Image")
                    }
                }
            }
        }
    )
}

@Composable
fun ZoomableImage(uri: Uri?) {

    if (uri == null) {
        return
    }

    val painter = rememberAsyncImagePainter(model = ImageRequest.Builder(LocalContext.current)
        .data(uri)
        .build()
    )

    Image(
        painter = painter,
        contentDescription = "Zoomable Image",
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 8.dp),
        contentScale = ContentScale.Crop
    )
}