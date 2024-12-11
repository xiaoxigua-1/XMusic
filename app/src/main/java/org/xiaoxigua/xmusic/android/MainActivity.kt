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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.videolan.libvlc.LibVLC
import org.xiaoxigua.xmusic.android.components.BottomBar
import org.xiaoxigua.xmusic.android.components.TopBar
import org.xiaoxigua.xmusic.android.core.MusicPlayer
import org.xiaoxigua.xmusic.android.room.UserViewModel
import org.xiaoxigua.xmusic.android.screens.Screens
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

val LocalLibVLC = compositionLocalOf<LibVLC> {
    error("Missing LibVLC")
}

val LocalNavController = compositionLocalOf<NavHostController> {
    error("Missing Nav Controller")
}

val LocalUserViewModel = compositionLocalOf<UserViewModel> {
    error("Missing UserViewModel")
}

val LocalMusicPlayer = compositionLocalOf<MusicPlayer> {
    error("Missing MusicPlayer")
}

@Composable
fun MainCompose() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val libVLC = LibVLC(context)
    val userViewModel: UserViewModel = viewModel()
    val musicPlayer = MusicPlayer(libVLC)

    CompositionLocalProvider(
        LocalLibVLC provides libVLC,
        LocalNavController provides navController,
        LocalUserViewModel provides userViewModel,
        LocalMusicPlayer provides musicPlayer
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = { BottomBar() },
            topBar = { TopBar() }) { innerPadding ->
            NavHost(
                navController,
                startDestination = "Home",
                modifier = Modifier.padding(innerPadding),
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None }) {
                Screens.entries.forEach { s ->

                    composable(s.route) { backStackEntry ->
                        s.screen(backStackEntry)
                    }
                }
            }
        }
    }
}