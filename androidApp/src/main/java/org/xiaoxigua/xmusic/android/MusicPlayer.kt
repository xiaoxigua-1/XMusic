package org.xiaoxigua.xmusic.android

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.sharp.SkipNext
import androidx.compose.material.icons.sharp.SkipPrevious
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.documentfile.provider.DocumentFile
import org.xiaoxigua.xmusic.MusicPlayer
import org.xiaoxigua.xmusic.Progress
import java.util.Locale

@Composable
fun OpenFolderScreen() {
    var selectedFolderUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val current = remember { mutableIntStateOf(0) }
    val musicPlayer = MusicPlayer(context, current.intValue)

    // 通过 rememberLauncherForActivityResult 创建一个 launcher 用于启动文件夹选择器
    val folderPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocumentTree()
    ) { uri: Uri? ->
        selectedFolderUri = uri
        if (uri != null) {
            // get folder read permission
            context.contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            )
        }
    }

    // on ui loaded
    LaunchedEffect(Unit) {
        folderPickerLauncher.launch(null)
    }

    selectedFolderUri?.let { uri ->
        val folder = DocumentFile.fromTreeUri(context, uri)

        folder?.listFiles()?.forEach { file ->
            if (file != null && file.name?.endsWith(".flac") == true) {
                musicPlayer.mediaList.addMedia(file.uri)
            }
        }
        musicPlayer.setCurrentMedia()
        musicPlayer.vlcPlayer.play()
        MusicPlayerScreen(musicPlayer)
    }
}

@Composable
fun MusicPlayerScreen(musicPlayer: MusicPlayer) {
    val currentMetas = musicPlayer.currentMeta()
    var progress by remember { mutableStateOf(musicPlayer.getProgress()) }
    var isPlaying by remember { mutableStateOf(musicPlayer.vlcPlayer.isPlaying) }

    musicPlayer.vlcPlayer.setUpdate({ data ->
        if (data.pos < 1f && data.pos > 0.001f) {
            progress = data
        }
    }, {
        musicPlayer.next()
        musicPlayer.vlcPlayer.play()
    })

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (currentMetas != null) {
                Text(text = currentMetas.title ?: "???", fontSize = 24.sp)
                Text(text = currentMetas.artist ?: "???", fontSize = 12.sp)

                Box(modifier = Modifier.padding(50.dp, 20.dp)) {
                    VinylAlbumCoverAnimation(isPlaying, currentMetas.artworkURL)
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp)
                ) {
                    Slider(progress.pos, onValueChange = {
                        musicPlayer.vlcPlayer.pause()
                        progress = Progress((progress.length * it).toLong(), it, progress.length)
                    }, onValueChangeFinished = {
                        musicPlayer.vlcPlayer.setPosition(progress.pos)
                        musicPlayer.vlcPlayer.play()
                    }, valueRange = 0f..1f, colors = SliderDefaults.colors(
                        thumbColor = Color.Black,
                        activeTrackColor = Color.DarkGray,
                        inactiveTrackColor = Color.Gray,
                    ), modifier = Modifier.height(1.dp)
                    )

                    Row {
                        Text(
                            text = formatDuration(progress.time),
                            modifier = Modifier
                                .weight(1f)
                                .padding(8.dp)
                        )

                        Text(
                            text = formatDuration(progress.length),
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ControlButton(Icons.Sharp.SkipPrevious, 64.dp) {
                        musicPlayer.prev()
                        musicPlayer.vlcPlayer.play()
                    }
                    ControlButton(
                        if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                        84.dp
                    ) {
                        isPlaying = !isPlaying
                        if (isPlaying) {
                            musicPlayer.vlcPlayer.play()
                        } else musicPlayer.vlcPlayer.pause()
                    }
                    ControlButton(Icons.Sharp.SkipNext, 64.dp) {
                        musicPlayer.next()
                        musicPlayer.vlcPlayer.play()
                    }
                }
            }
        }
    }
}

fun formatDuration(milliseconds: Long): String {
    val totalSeconds = (milliseconds / 1000).toInt()
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60

    return String.format(Locale.US, "%02d:%02d", minutes, seconds)
}

@Composable
fun ControlButton(icon: ImageVector, size: Dp, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .clickable {
                onClick()
            }, contentAlignment = Alignment.Center
    ) {
        Icon(
            icon,
            modifier = Modifier.size(size / 1.5f),
            tint = Color.Black,
            contentDescription = null
        )
    }
}

@Preview
@Composable
fun PreviewOpenFolderScreen() {
    OpenFolderScreen()
}