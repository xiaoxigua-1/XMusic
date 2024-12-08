package org.xiaoxigua.xmusic.android.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Radio
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import org.xiaoxigua.xmusic.android.components.AddPlaylist
import org.xiaoxigua.xmusic.android.room.UserViewModel

enum class Screens(
    val route: String,
    val title: String? = route,
    val icon: ImageVector? = null,
    val screen: @Composable (NavHostController, NavBackStackEntry, UserViewModel) -> Unit,
    val rightButton: @Composable (UserViewModel) -> Unit = {},
    val canBack: Boolean = false
) {
    Home(route = "Home", icon = Icons.Filled.Home, screen = { navController, _, userViewModel ->
        HomeScreen(navController, userViewModel)
    }, rightButton = { AddPlaylist(it) }),
    Radio(route = "Radio", icon = Icons.Filled.Radio, screen = { _, _, _ -> }),
    Search(route = "Search", icon = Icons.Filled.Search, screen = { _, _, _ -> }),
    Playlist(
        route = "Home/{playlistId}",
        icon = null,
        screen = { _, backStackEntry, userViewModel ->
            val playlistId = backStackEntry.arguments?.getString("playlistId")

            if (playlistId != null)
                PlaylistScreen(userViewModel, playlistId.toLong())
        },
        rightButton = {},
        canBack = true,
        title = null
    )
}