package org.xiaoxigua.xmusic.android

import androidx.compose.runtime.Composable

@Composable
fun PlaylistList() {
    val currentPlaylist = LocalCurrentPlaylist.current
    val musicPlayer = LocalMusicPlayer.current
}