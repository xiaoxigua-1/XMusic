package org.xiaoxigua.xmusic.android

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.xiaoxigua.xmusic.Progress
import org.xiaoxigua.xmusic.android.components.PlayerControl
import org.xiaoxigua.xmusic.android.components.ProgressSlider
import org.xiaoxigua.xmusic.android.components.VinylAlbumCoverAnimation
import kotlin.math.abs

enum class ScreenState {
    Maximum,
    Minimum
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MusicPlayerScreenAnchoredDraggable() {
    val currentPlayList = LocalCurrentPlaylist.current.intValue
    val musicPlayer = LocalMusicPlayer.current
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    val screenHeightDp = configuration.screenHeightDp.dp
    val screenHeightPx = with(density) { (screenHeightDp - 80.dp).toPx() }
    val anchors = DraggableAnchors {
        ScreenState.Minimum at screenHeightPx
        ScreenState.Maximum at 0.dp.value
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
    val current = remember { mutableIntStateOf(0) }
    val progress = remember { mutableStateOf(musicPlayer.getProgress()) }
    val isPlaying = remember { mutableStateOf(musicPlayer.vlcPlayer.isPlaying) }

    musicPlayer.vlcPlayer.setUpdate({ data ->
        if (data.pos < 1f && data.pos > 0.001f) {
            progress.value = data
        }
    }, {
        current.intValue = musicPlayer.next(currentPlayList, current.intValue)
        musicPlayer.vlcPlayer.play()
    })

    LaunchedEffect(current) {
        progress.value = musicPlayer.getProgress()
    }

    Box(
        modifier = Modifier
            .offset {
                IntOffset(
                    0,
                    state
                        .requireOffset()
                        .toInt()
                )
            }
            .anchoredDraggable(state, Orientation.Vertical),
    ) {
        Column {
            AnimatedVisibility(
                visible = state.offset > screenHeightPx - 80,
                modifier = Modifier.alpha(abs(state.offset / screenHeightPx)),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                MusicPlayerMinimumScreen(isPlaying, current, progress)
            }
            Box(
                modifier = Modifier.alpha(1f - abs(state.offset / screenHeightPx))
            ) {
                MusicPlayerScreen(isPlaying, current, progress)
            }
        }
    }
}

@Composable
fun MusicPlayerScreen(
    isPlaying: MutableState<Boolean>,
    current: MutableIntState,
    progress: MutableState<Progress>
) {
    val musicPlayer = LocalMusicPlayer.current
    val currentPlayList = LocalCurrentPlaylist.current.intValue
    val currentMetas = musicPlayer.getMeta(currentPlayList, current.intValue)

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.weight(1f))
        VinylAlbumCoverAnimation(isPlaying.value, currentMetas?.artworkURL)

        Column(
            modifier = Modifier
                .padding(10.dp, 20.dp)
                .align(Alignment.Start)
        ) {
            Text(
                text = currentMetas?.title ?: "Unknown title", fontSize = 24.sp
            )
            Text(text = currentMetas?.artist ?: "Unknown Artist", fontSize = 12.sp)
        }

        ProgressSlider(progress, isPlaying.value, musicPlayer, false)
        PlayerControl(current, isPlaying, 64.dp)
    }
}

@Composable
fun MusicPlayerMinimumScreen(
    isPlaying: MutableState<Boolean>,
    current: MutableIntState,
    progress: MutableState<Progress>
) {
    val musicPlayer = LocalMusicPlayer.current
    val currentPlayList = LocalCurrentPlaylist.current.intValue
    val currentMetas = musicPlayer.getMeta(currentPlayList, current.intValue)

    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(vertical = 4.dp, horizontal = 10.dp)
                .fillMaxWidth()
        ) {
            Box(modifier = Modifier.size(64.dp)) {
                VinylAlbumCoverAnimation(isPlaying.value, currentMetas?.artworkURL)
            }
            Column {
                Text(
                    text = currentMetas?.title ?: "Unknown title", fontSize = 16.sp
                )
                Text(text = currentMetas?.artist ?: "Unknown Artist", fontSize = 8.sp)
            }
            PlayerControl(current, isPlaying, 48.dp)
        }

        ProgressSlider(progress, isPlaying.value, musicPlayer, true)
    }
}