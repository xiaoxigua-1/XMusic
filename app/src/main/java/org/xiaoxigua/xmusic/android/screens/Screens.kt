package org.xiaoxigua.xmusic.android.screens

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Radio
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.documentfile.provider.DocumentFile
import androidx.navigation.NavBackStackEntry
import org.xiaoxigua.xmusic.android.LocalLibVLC
import org.xiaoxigua.xmusic.android.LocalUserViewModel
import org.xiaoxigua.xmusic.android.components.AddButton
import org.xiaoxigua.xmusic.android.components.AddButtonAlertDialog
import org.xiaoxigua.xmusic.android.core.data.MediaData
import org.xiaoxigua.xmusic.android.room.entity.Playlist

enum class Screens(
    val route: String,
    val title: String? = route,
    val icon: ImageVector? = null,
    val screen: @Composable (NavBackStackEntry) -> Unit,
    val rightButton: @Composable (NavBackStackEntry?) -> Unit = {},
    val canBack: Boolean = false
) {
    Home(route = "Home", icon = Icons.Filled.Home, screen = { _ ->
        HomeScreen()
    }, rightButton = { _ ->
        val userViewModel = LocalUserViewModel.current

        AddButton(content = {
            AddButtonAlertDialog(
                "Add Playlist",
                "Add",
                { title, description ->
                    val playlist = Playlist(
                        title = title,
                        description = description
                    )

                    userViewModel.addPlaylist(playlist)
                    it.value = false
                },
                {
                    it.value = false
                })
        })
    }),
    Radio(route = "Radio", icon = Icons.Filled.Radio, screen = { _ -> }),
    Search(route = "Search", icon = Icons.Filled.Search, screen = { _ -> }),
    Playlist(
        route = "Home/{playlistId}",
        icon = null,
        screen = { backStackEntry ->
            val playlistId = backStackEntry.arguments?.getString("playlistId")
            val userViewModel = LocalUserViewModel.current

            if (playlistId != null)
                PlaylistScreen(userViewModel, playlistId.toLong())
        },
        canBack = true,
        title = null,
        rightButton = { backStackEntry ->
            val context = LocalContext.current
            val libVLC = LocalLibVLC.current
            val userViewModel = LocalUserViewModel.current
            var selectedFolderUri by remember { mutableStateOf<Uri?>(null) }
            val playlistId = backStackEntry?.arguments?.getString("playlistId")!!
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

            LaunchedEffect(selectedFolderUri) {
                selectedFolderUri?.let { uri ->
                    val folder = DocumentFile.fromTreeUri(context, uri)

                    folder?.listFiles()?.forEach { file ->
                        if (file != null && file.isFile) {
                            val media = MediaData.init(libVLC, playlistId.toLong(), file.uri)
                            val metas = media.getMediaMetas()

                            metas?.let {
                                userViewModel.addSong(metas)
                            }
                        }
                    }
                }
            }

            AddButton(onClickable = {
                folderPickerLauncher.launch(null)
            })
        }
    )
}