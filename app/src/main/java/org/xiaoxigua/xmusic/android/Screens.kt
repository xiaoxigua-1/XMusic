package org.xiaoxigua.xmusic.android

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Radio
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import org.xiaoxigua.xmusic.android.components.AddPlaylist
import org.xiaoxigua.xmusic.android.room.UserViewModel

enum class Screens(
    val route: String,
    val icon: ImageVector,
    val screen: @Composable (userViewModel: UserViewModel) -> Unit,
    val rightButton: @Composable (userViewModel: UserViewModel) -> Unit = {},
    val canBack: Boolean = false
) {
    Home("Home", Icons.Filled.Home, { HomeScreen(it) }, { AddPlaylist(it) }),
    Radio("Radio", Icons.Filled.Radio, {}),
    Search("Search", Icons.Filled.Search, {})
}