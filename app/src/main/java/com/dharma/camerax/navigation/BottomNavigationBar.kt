package com.dharma.camerax.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.dharma.camerax.R

@Composable
fun BottomNavigationBar(navController: NavController) {

    NavigationBar {

        NavigationBarItem(
            label = {Text(stringResource(R.string.all_images))},
            selected = navController.currentBackStackEntry?.destination?.route ==AppScreens.AllImagesScreen.route,
            onClick = {
                navController.navigate(AppScreens.AllImagesScreen.route){
                    popUpTo(navController.graph.startDestinationId){inclusive=true}
                }
            },
            icon = { Icon(painter = painterResource(id = R.drawable.ic_image), contentDescription = stringResource(R.string.all_images)) }
        )

        NavigationBarItem(

            label = {Text(stringResource(R.string.settings))},
            selected = navController.currentBackStackEntry?.destination?.route ==AppScreens.SettingsScreen.route,
            onClick = {
                navController.navigate(AppScreens.SettingsScreen.route)
            },
            icon = { Icon(Icons.Default.Settings, contentDescription = stringResource(R.string.settings)) }
        )
    }
}