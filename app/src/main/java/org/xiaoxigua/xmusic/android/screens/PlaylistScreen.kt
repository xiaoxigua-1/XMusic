package org.xiaoxigua.xmusic.android.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import org.xiaoxigua.xmusic.android.components.SongItem
import org.xiaoxigua.xmusic.android.room.UserViewModel

@Composable
fun PlaylistScreen(userViewModel: UserViewModel, playlistId: Long) {
    val songs by userViewModel.queryPlaylistSongs(playlistId).observeAsState(emptyList())

    LazyColumn {
        items(songs, key = { it.songId }) { song ->
            SongItem(song)
        }
    }
}