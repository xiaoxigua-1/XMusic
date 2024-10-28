package org.xiaoxigua.xmusic.android.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.sharp.SkipNext
import androidx.compose.material.icons.sharp.SkipPrevious
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import org.xiaoxigua.xmusic.android.LocalCurrentPlaylist
import org.xiaoxigua.xmusic.android.LocalMusicPlayer

@Composable
fun PlayerControl(current: MutableIntState, isPlaying: MutableState<Boolean>, size: Dp) {
    val musicPlayer = LocalMusicPlayer.current
    val currentPlayList = LocalCurrentPlaylist.current.intValue

    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ControlButton(Icons.Sharp.SkipPrevious, size) {
            current.intValue = musicPlayer.prev(currentPlayList, current.intValue)
            isPlaying.value = musicPlayer.vlcPlayer.play()
        }
        ControlButton(
            if (isPlaying.value) Icons.Filled.Pause else Icons.Filled.PlayArrow,
            size
        ) {
            isPlaying.value = if (!isPlaying.value) {
                musicPlayer.vlcPlayer.play()
            } else !musicPlayer.vlcPlayer.pause()
        }
        ControlButton(Icons.Sharp.SkipNext, size) {
            current.intValue = musicPlayer.next(currentPlayList, current.intValue)
            isPlaying.value = musicPlayer.vlcPlayer.play()
        }
    }
}