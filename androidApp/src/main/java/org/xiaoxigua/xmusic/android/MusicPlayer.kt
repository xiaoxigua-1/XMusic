package org.xiaoxigua.xmusic.android

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.OpenableColumns
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
import androidx.compose.runtime.MutableState
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
    val musicPlayer = MusicPlayer(context)

    // Create a launcher using rememberLauncherForActivityResult to launch a file selector.
    val folderPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocumentTree()
    ) { uri: Uri? ->
        selectedFolderUri = uri
        if (uri != null) {
            // Request folder read access.
            context.contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            )
        }
    }

    // on ui loaded
    LaunchedEffect(Unit) {
        if (selectedFolderUri == null)
            folderPickerLauncher.launch(null)
    }

    selectedFolderUri?.let { uri ->
        val folder = DocumentFile.fromTreeUri(context, uri)

        folder?.listFiles()?.forEach { file ->
            if (file != null && file.isFile) {
                musicPlayer.mediaList.addMedia(file.uri)
            }
        }
        musicPlayer.setMedia(0)
        musicPlayer.vlcPlayer.play()
        MusicPlayerScreen(musicPlayer)
    }
}

@Composable
fun MusicPlayerScreen(musicPlayer: MusicPlayer) {
    var current by remember { mutableIntStateOf(0) }
    val currentMetas = musicPlayer.getMeta(current)
    val context = LocalContext.current
    val progress = remember { mutableStateOf(musicPlayer.getProgress()) }
    var isPlaying by remember { mutableStateOf(musicPlayer.vlcPlayer.isPlaying) }

    musicPlayer.vlcPlayer.setUpdate({ data ->
        if (data.pos < 1f && data.pos > 0.001f) {
            progress.value = data
        }
    }, {
        current = musicPlayer.next(current)
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
                Text(
                    text = currentMetas.title ?: getFileName(
                        context,
                        musicPlayer.getUri(current) as Uri
                    )!!, fontSize = 24.sp
                )
                Text(text = currentMetas.artist ?: "Unknown Artist", fontSize = 12.sp)

                Box(modifier = Modifier.padding(50.dp, 20.dp)) {
                    VinylAlbumCoverAnimation(isPlaying, currentMetas.artworkURL)
                }

                ProgressSlider(progress, isPlaying, musicPlayer)

                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ControlButton(Icons.Sharp.SkipPrevious, 64.dp) {
                        current = musicPlayer.prev(current)
                        musicPlayer.vlcPlayer.play()
                        isPlaying = true
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
                        current = musicPlayer.next(current)
                        musicPlayer.vlcPlayer.play()
                        isPlaying = true
                    }
                }
            }
        }
    }
}

@Composable
fun ProgressSlider(progress: MutableState<Progress>, isPlaying: Boolean, musicPlayer: MusicPlayer) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp)
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
            thumbColor = Color.Black,
            activeTrackColor = Color.DarkGray,
            inactiveTrackColor = Color.Gray,
        ), modifier = Modifier.height(1.dp)
        )

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

fun getFileName(context: Context, uri: Uri): String? {
    var fileName: String? = null
    // 使用 ContentResolver 查詢 URI
    context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
        if (cursor.moveToFirst()) {
            // 查找檔案名稱的欄位 (通常是 OpenableColumns.DISPLAY_NAME)
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (nameIndex != -1) {
                fileName = cursor.getString(nameIndex)
            }
        }
    }

    if (fileName != null) {
        // 去掉副檔名
        val dotIndex = fileName!!.lastIndexOf('.')
        if (dotIndex != -1) {
            fileName = fileName!!.substring(0, dotIndex)
        }
    }
    return fileName
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