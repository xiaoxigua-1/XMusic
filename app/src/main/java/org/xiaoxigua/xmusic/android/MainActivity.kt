package org.xiaoxigua.xmusic.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.xiaoxigua.xmusic.android.components.BottomBar
import org.xiaoxigua.xmusic.android.components.TopBar
import org.xiaoxigua.xmusic.android.room.UserViewModel
import org.xiaoxigua.xmusic.android.ui.theme.XMusicTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            XMusicTheme {
                MainCompose()
            }
        }
    }
}

@Composable
fun MainCompose(userViewModel: UserViewModel = viewModel()) {
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { BottomBar(navController) },
        topBar = { TopBar(navController, userViewModel) }) { innerPadding ->
        NavHost(
            navController,
            startDestination = "Home",
            modifier = Modifier.padding(innerPadding),
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }) {
            Screens.entries.forEach { s ->
                composable(s.route) {
                    s.screen(userViewModel)
                }
            }
        }
    }
}