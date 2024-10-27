package org.xiaoxigua.xmusic.android.components

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.documentfile.provider.DocumentFile
import org.xiaoxigua.xmusic.AndroidMusicPlayer
import org.xiaoxigua.xmusic.MusicPlayer
import org.xiaoxigua.xmusic.getPlatformMediaList

@Composable
fun OpenFolderScreen(musicPlayer: MusicPlayer, importIndex: Int) {
    var selectedFolderUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current

    // Create a launcher using rememberLauncherForActivityResult to launch a file selector.
    val folderPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocumentTree()
    ) { uri ->
        selectedFolderUri = uri
        if (uri != null) {
            // Request folder read access.
            context.contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            )
        }
    }

    LaunchedEffect(selectedFolderUri) {
        selectedFolderUri?.let { uri ->
            val folder = DocumentFile.fromTreeUri(context, uri)

            folder?.listFiles()?.forEach { file ->
                if (file != null && file.isFile) {
                    musicPlayer.getCurrentPlayList(importIndex)?.addMedia(file.uri)
                }
            }
        }
    }

    // Add icon Button
    ControlButton(Icons.Filled.Add, 40.dp) {
        folderPickerLauncher.launch(null)
    }
}

@Preview
@Composable
fun PreviewOpenFolderScreen() {
    val musicPlayer = AndroidMusicPlayer(LocalContext.current)

    musicPlayer.addPlaylist(getPlatformMediaList())

    OpenFolderScreen(musicPlayer, 0)
}