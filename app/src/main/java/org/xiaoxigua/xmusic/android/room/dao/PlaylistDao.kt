package org.xiaoxigua.xmusic.android.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import org.xiaoxigua.xmusic.android.room.entity.Playlist
import org.xiaoxigua.xmusic.android.room.entity.Song

@Dao
interface PlaylistDao {
    @Query("SELECT * FROM Playlist")
    fun getPlaylists(): LiveData<List<Playlist>>

    @Query("SELECT * FROM Song WHERE playlistId = :playlistId")
    fun queryPlaylistSongs(playlistId: Long): LiveData<List<Song>>

    @Insert
    fun addPlaylist(playlist: Playlist)

    @Insert
    fun addSong(song: Song)

    @Delete
    fun deletePlaylist(playlist: Playlist)
}