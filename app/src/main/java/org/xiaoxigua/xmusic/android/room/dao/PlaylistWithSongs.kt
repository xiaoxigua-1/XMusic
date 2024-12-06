package org.xiaoxigua.xmusic.android.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.Junction
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Transaction
import org.xiaoxigua.xmusic.android.room.entity.Playlist
import org.xiaoxigua.xmusic.android.room.entity.PlaylistSongCrossRef
import org.xiaoxigua.xmusic.android.room.entity.Song

data class PlaylistWithSongs(
    @Embedded val playlist: Playlist,
    @Relation(
        parentColumn = "playlistId",
        entityColumn = "songId",
        associateBy = Junction(PlaylistSongCrossRef::class)
    )
    val songs: List<Song>
)

@Dao
interface PlaylistDao {
    @Query("SELECT * FROM Playlist")
    fun getPlaylists(): LiveData<List<Playlist>>

    @Transaction
    @Query("SELECT * FROM Playlist WHERE playlistId = :playlistId")
    fun queryPlaylistSongs(playlistId: Long): PlaylistWithSongs

    @Insert
    fun addPlaylist(playlist: Playlist)

    @Delete
    fun deletePlaylist(playlist: Playlist)

    @Insert
    fun addSongToPlaylist(ref: PlaylistSongCrossRef)
}