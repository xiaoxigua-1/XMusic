package org.xiaoxigua.xmusic.android

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.sharp.SkipNext
import androidx.compose.material.icons.sharp.SkipPrevious
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.xiaoxigua.xmusic.android.components.ControlButton
import org.xiaoxigua.xmusic.android.components.ProgressSlider
import org.xiaoxigua.xmusic.android.components.VinylAlbumCoverAnimation

enum class ScreenState {
    Maximum,
    Minimum
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MusicPlayerScreen() {
    val currentPlayList = LocalCurrentPlaylist.current.intValue
    val musicPlayer = LocalMusicPlayer.current
    val anchors = DraggableAnchors {
        ScreenState.Maximum at 100.dp.value
        ScreenState.Minimum at -100.dp.value
    }
    val state = remember {
        AnchoredDraggableState(
            ScreenState.Maximum,
            anchors,
            { totalDistance ->
                totalDistance * 0.5f
            },
            {
                56.dp.value
            },
            tween(durationMillis = 300, easing = FastOutSlowInEasing),
            {
                true
            }
        )
    }
    var current by remember { mutableIntStateOf(0) }
    val progress = remember { mutableStateOf(musicPlayer.getProgress()) }
    var isPlaying by remember { mutableStateOf(musicPlayer.vlcPlayer.isPlaying) }
    val currentMetas = musicPlayer.getMeta(currentPlayList, current)

    musicPlayer.vlcPlayer.setUpdate({ data ->
        if (data.pos < 1f && data.pos > 0.001f) {
            progress.value = data
        }
    }, {
        current = musicPlayer.next(currentPlayList, current)
        musicPlayer.vlcPlayer.play()
    })

    LaunchedEffect(current) {
        progress.value = musicPlayer.getProgress()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .offset { IntOffset(x = 0, y = state.requireOffset().toInt()) }
            .anchoredDraggable(state, Orientation.Vertical),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = currentMetas?.title ?: "Unknown title", fontSize = 24.sp
            )
            Text(text = currentMetas?.artist ?: "Unknown Artist", fontSize = 12.sp)

            Box(modifier = Modifier.padding(50.dp, 20.dp)) {
                VinylAlbumCoverAnimation(isPlaying, currentMetas?.artworkURL)
            }

            ProgressSlider(progress, isPlaying, musicPlayer)

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ControlButton(Icons.Sharp.SkipPrevious, 64.dp) {
                    current = musicPlayer.prev(currentPlayList, current)
                    isPlaying = musicPlayer.vlcPlayer.play()
                }
                ControlButton(
                    if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                    86.dp
                ) {
                    isPlaying = if (!isPlaying) {
                        musicPlayer.vlcPlayer.play()
                    } else !musicPlayer.vlcPlayer.pause()
                }
                ControlButton(Icons.Sharp.SkipNext, 64.dp) {
                    current = musicPlayer.next(currentPlayList, current)
                    isPlaying = musicPlayer.vlcPlayer.play()
                }
            }
        }
    }
}