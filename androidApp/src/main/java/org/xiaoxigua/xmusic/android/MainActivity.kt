package org.xiaoxigua.xmusic.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import org.xiaoxigua.xmusic.AndroidMusicPlayer
import org.xiaoxigua.xmusic.MusicPlayer

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Main()
                }
            }
        }
    }
}

val LocalCurrentPlaylist = compositionLocalOf { mutableIntStateOf(0) }
val LocalMusicPlayer = compositionLocalOf<MusicPlayer> { error("MusicPlayer error") }

@Composable
fun Main() {
    val musicPlayer = AndroidMusicPlayer(LocalContext.current)
    val currentPlayList = remember { mutableIntStateOf(0) }

    CompositionLocalProvider(
        LocalCurrentPlaylist provides currentPlayList,
        LocalMusicPlayer provides musicPlayer
    ) {
        PlaylistList()
        MusicPlayerScreenAnchoredDraggable()
    }
}

@Preview
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        Main()
    }
}
