package com.dharma.camerax.navigation

import AllImagesScreen
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dharma.camerax.CaptureImagesViewModel
import com.dharma.camerax.capture.CapturePermissionsScreen
import com.dharma.camerax.image_details.ImageDetailsScreen
import com.dharma.camerax.settings.SettingsScreen
import java.util.concurrent.ExecutorService

@Composable
fun AppNavigation (
    navaController : NavHostController,
    cameraExecutor : ExecutorService) {

        NavHost(
            navController = navaController,
            startDestination = AppScreens.AllImagesScreen.route,
        ){

            composable(AppScreens.AllImagesScreen.route){
                AllImagesScreen(
                    navController = navaController
                )
            }

            composable(AppScreens.SettingsScreen.route){
                SettingsScreen(
                    navController = navaController
                )
            }

            composable(AppScreens.CameraScreen.route) {

                val viewModel: CaptureImagesViewModel = hiltViewModel()

                CapturePermissionsScreen(
                    cameraExecutor,
                    navController = navaController,
                    viewModel
                )
            }

            composable(route = "image_details_screen/{imageUri}") { navBackStackEntry ->
                val imageUri = navBackStackEntry.arguments?.getString("imageUri")
                ImageDetailsScreen(
                    navController = navaController,
                    imageUri = Uri.parse(imageUri)
                )
            }
        }

}

sealed class AppScreens(val route : String) {
    data object AllImagesScreen : AppScreens("all_images_screen")
    data object CameraScreen : AppScreens("camera_screen")
    data object SettingsScreen : AppScreens("settings_screen")
    data class ImageDetailsScreen(val imageUri: Uri) : AppScreens("image_details_screen/$imageUri")
}


