package org.xiaoxigua.xmusic.android.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import org.xiaoxigua.xmusic.android.LocalMusicPlayer
import org.xiaoxigua.xmusic.android.components.SongItem
import org.xiaoxigua.xmusic.android.core.data.PlaylistData
import org.xiaoxigua.xmusic.android.room.UserViewModel

@Composable
fun PlaylistScreen(userViewModel: UserViewModel, playlistId: Long) {
    val musicPlayer = LocalMusicPlayer.current
    val nowPlaying by musicPlayer.nowPlaylist.observeAsState()
    val nowPlayingIndex = nowPlaying?.index?.observeAsState()
    val songs by userViewModel.queryPlaylistSongs(playlistId).observeAsState(emptyList())

    LazyColumn(
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        itemsIndexed(songs, key = { _, song -> song.songId }) { index, song ->
            SongItem(song, nowPlaying?.playlist == playlistId && nowPlayingIndex?.value == index) {
                musicPlayer.setPlaylist(PlaylistData(playlistId, songs, MutableLiveData(index)))
            }
        }
    }
}