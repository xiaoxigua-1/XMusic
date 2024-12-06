package org.xiaoxigua.xmusic.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.xiaoxigua.xmusic.android.components.BottomBar
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
fun MainCompose() {
    val navController = rememberNavController()

    Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = { BottomBar(navController) }) { innerPadding ->
        NavHost(navController, startDestination = "Home", modifier = Modifier.padding(innerPadding)) {
            Screens.entries.forEach { s ->
                composable(s.route) {
                    s.screen()
                }
            }
        }
    }
}