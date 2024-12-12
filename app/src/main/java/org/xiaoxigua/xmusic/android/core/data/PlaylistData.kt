package org.xiaoxigua.xmusic.android.core.data

import androidx.lifecycle.MutableLiveData
import org.xiaoxigua.xmusic.android.room.entity.Song

data class PlaylistData(val playlist: Long?, val songs: List<Song>, val index: MutableLiveData<Int>)