package org.xiaoxigua.xmusic.android.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.xiaoxigua.xmusic.MusicPlayer
import org.xiaoxigua.xmusic.Progress
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressSlider(
    progress: MutableState<Progress>,
    isPlaying: Boolean,
    musicPlayer: MusicPlayer,
    hidTime: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Slider(progress.value.pos, onValueChange = {
            musicPlayer.vlcPlayer.pause()
            progress.value =
                Progress((progress.value.length * it).toLong(), it, progress.value.length)
        }, onValueChangeFinished = {
            musicPlayer.vlcPlayer.setPosition(progress.value.pos)
            if (isPlaying)
                musicPlayer.vlcPlayer.play()
        }, valueRange = 0f..1f, colors = SliderDefaults.colors(
            thumbColor = Color.Transparent,
        ), modifier = Modifier.height(0.dp),
            thumb = {
                Box(modifier = Modifier.size(0.dp))
            }
        )

        if (!hidTime)
            Row {
                Text(
                    text = formatDuration(progress.value.time),
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                )

                Text(
                    text = formatDuration(progress.value.length),
                    modifier = Modifier.padding(8.dp)
                )
            }
    }
}

fun formatDuration(milliseconds: Long): String {
    val totalSeconds = (milliseconds / 1000).toInt()
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60

    return String.format(Locale.US, "%02d:%02d", minutes, seconds)
}