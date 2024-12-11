package org.xiaoxigua.xmusic.android.core.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.xiaoxigua.xmusic.android.room.entity.Playlist
import org.xiaoxigua.xmusic.android.room.entity.Song

data class PlaylistData(val playlist: LiveData<Playlist>, val songs: LiveData<List<Song>>, val index: MutableLiveData<Int>)
