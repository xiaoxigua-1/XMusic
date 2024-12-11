package org.xiaoxigua.xmusic.android.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.xiaoxigua.xmusic.android.LocalNavController
import org.xiaoxigua.xmusic.android.LocalUserViewModel
import org.xiaoxigua.xmusic.android.components.PlaylistItem
import org.xiaoxigua.xmusic.android.ui.theme.XMusicTheme

@Composable
fun HomeScreen() {
    val navController = LocalNavController.current
    val userViewModel = LocalUserViewModel.current
    val playlist by userViewModel.allPlaylist.observeAsState(emptyList())

    LazyColumn(modifier = Modifier.padding(vertical = 10.dp)) {
        items(playlist, key = { it.playlistId }) {
            PlaylistItem(it.title, it.description, {}, {
                userViewModel.deletePlaylist(it)
            }, {
                navController.navigate("Home/${it.playlistId}")
            })
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    XMusicTheme {
        HomeScreen()
    }
}