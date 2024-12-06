package org.xiaoxigua.xmusic.android.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import org.xiaoxigua.xmusic.android.room.UserViewModel
import org.xiaoxigua.xmusic.android.room.entity.Playlist
import org.xiaoxigua.xmusic.android.ui.theme.ContainerColor
import org.xiaoxigua.xmusic.android.ui.theme.DisabledLightGray
import org.xiaoxigua.xmusic.android.ui.theme.Purple

@Composable
fun AddPlaylist(userViewModel: UserViewModel) {
    var openDialog by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    TextButton(
        {
            openDialog = true
        }, colors = ButtonColors(
            containerColor = Color.Transparent,
            contentColor = Purple,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = DisabledLightGray
        )
    ) {
        Text("Add", color = Purple)
    }

    if (openDialog)
        AlertDialog(
            title = {
                Text(text = "Add Playlist")
            },
            onDismissRequest = {
                openDialog = false
            },
            text = {
                Column {
                    OutlinedTextField(
                        title,
                        onValueChange = { title = it },
                        label = { Text("Title") }
                    )
                    OutlinedTextField(
                        description,
                        onValueChange = { description = it },
                        label = { Text("Description") }
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val playlist = Playlist(title = title, description = description)

                        userViewModel.addPlaylist(playlist)

                        title = ""
                        description = ""
                        openDialog = false
                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        title = ""
                        description = ""
                        openDialog = false
                    }
                ) {
                    Text("Dismiss", color = DisabledLightGray)
                }
            },
            containerColor = ContainerColor
        )
}
