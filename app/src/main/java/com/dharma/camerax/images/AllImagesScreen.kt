import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.dharma.camerax.R
import com.dharma.camerax.images.viewmodel.AllImagesViewModel
import com.dharma.camerax.navigation.AppScreens
import com.dharma.camerax.navigation.BottomNavigationBar
import com.dharma.camerax.ui.theme.CameraxTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllImagesScreen(
    navController: NavController,
    viewModel: AllImagesViewModel = hiltViewModel()
) {
    val capturedImages by viewModel.capturedImages.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadImagesFromDirectory()
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = stringResource(R.string.all_images))})
        },
        content = { paddingValues ->
            // Use Box to freely position the FAB at the bottom-right
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues) // Add padding so the button does not touch the screen edges
            ) {
                // Main content in the center
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Display captured images
                    if (capturedImages.isNotEmpty()) {
                        PaginatedImagesGrid(
                            capturedImages = capturedImages,
                            onLoadMore = {
                                viewModel.loadNextPage()
                            },
                            navigateToImageDetails = { uri ->
                                navController.navigate("image_details_screen/${Uri.encode(uri.toString())}")
                            }
                        )
                    } else {
                        Text(stringResource(R.string.no_images_captured_yet_click_on_capture_image_button_to_capture_images))
                    }
                }

                // ExtendedFloatingActionButton with icon and text at the bottom-right
                ExtendedFloatingActionButton(
                    onClick = { navController.navigate(AppScreens.CameraScreen.route) },
                    text = { Text(stringResource(R.string.capture_image)) },  // Text in the FAB
                    icon = { Icon(painter = painterResource(id = R.drawable.ic_camera), contentDescription = stringResource(R.string.camera_icon)) },  // Icon in the FAB
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)  // Padding around the FAB for spacing from edges
                )
            }
        },
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    )


}

@Preview
@Composable
fun AllImagesScreenPreview() {
    CameraxTheme {
        AllImagesScreen(
            navController = rememberNavController()
        )
    }

}