package org.xiaoxigua.xmusic.android.room

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.xiaoxigua.xmusic.android.room.dao.PlaylistWithSongs
import org.xiaoxigua.xmusic.android.room.entity.Playlist

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val db =
        Room.databaseBuilder(application, AppDatabase::class.java, "app_database").build()
    private val playlistDao = db.playlistDao()

    val allPlaylist: LiveData<List<Playlist>> = playlistDao.getPlaylists()

    fun queryPlaylistSongs(playlistId: Long): LiveData<PlaylistWithSongs> {
        val result = MutableLiveData<PlaylistWithSongs>()

        viewModelScope.launch(Dispatchers.IO) {
            result.value = playlistDao.queryPlaylistSongs(playlistId)
        }

        return result
    }

    fun addPlaylist(playlist: Playlist) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistDao.addPlaylist(playlist)
        }
    }
}