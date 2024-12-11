package org.xiaoxigua.xmusic.android.room

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.xiaoxigua.xmusic.android.room.entity.Playlist
import org.xiaoxigua.xmusic.android.room.entity.Song

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val db =
        Room.databaseBuilder(application, AppDatabase::class.java, "testdb")
            .fallbackToDestructiveMigration()
            .build()
    private val playlistDao = db.playlistDao()

    val allPlaylist: LiveData<List<Playlist>> = playlistDao.getPlaylists()

    fun queryPlaylistSongs(playlistId: Long): LiveData<List<Song>> {
        return playlistDao.queryPlaylistSongs(playlistId)
    }

    fun addPlaylist(playlist: Playlist) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistDao.addPlaylist(playlist)
        }
    }

    fun addSong(song: Song) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistDao.addSong(song)
        }
    }

    fun deletePlaylist(playlist: Playlist) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistDao.deletePlaylist(playlist)
        }
    }
}