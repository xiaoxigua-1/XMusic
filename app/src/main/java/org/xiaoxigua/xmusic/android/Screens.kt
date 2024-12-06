package org.xiaoxigua.xmusic.android

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Radio
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

enum class Screens(val route: String, val icon: ImageVector, val screen: @Composable () -> Unit) {
    Home("Home", Icons.Filled.Home, { HomeScreen() }),
    Radio("Radio", Icons.Filled.Radio, {}),
    Search("Search", Icons.Filled.Search, {})
}