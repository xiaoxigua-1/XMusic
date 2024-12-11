package org.xiaoxigua.xmusic.android.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.currentBackStackEntryAsState
import org.xiaoxigua.xmusic.android.LocalNavController
import org.xiaoxigua.xmusic.android.screens.Screens
import org.xiaoxigua.xmusic.android.ui.theme.ContainerColor
import org.xiaoxigua.xmusic.android.ui.theme.Purple
import org.xiaoxigua.xmusic.android.ui.theme.XMusicTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar() {
    val navController = LocalNavController.current
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: ""
    val screen = Screens.entries.findLast { it.route == currentRoute }

    TopAppBar(
        title = { Text(screen?.title ?: "", fontSize = 32.sp, fontWeight = FontWeight.Bold) },
        navigationIcon = {
            if (screen?.canBack == true)
                IconButton(onClick = {
                    navController.popBackStack()
                }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBackIosNew,
                        contentDescription = null
                    )
                }
        },
        actions = {
            Screens.entries.findLast { it.route == currentRoute }?.rightButton?.let {
                it(
                    navBackStackEntry
                )
            }
        },
        colors = TopAppBarColors(
            containerColor = ContainerColor,
            scrolledContainerColor = Purple,
            navigationIconContentColor = Color.White,
            titleContentColor = Color.White,
            actionIconContentColor = Purple
        )
    )
}

@Preview
@Composable
fun TopBarPreview() {
    XMusicTheme {
        Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
            TopBar()
        }) { innerPadding ->
            Text("", modifier = Modifier.padding(innerPadding))
        }
    }
}