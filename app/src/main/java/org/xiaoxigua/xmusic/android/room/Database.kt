package org.xiaoxigua.xmusic.android.room

import androidx.room.Database
import androidx.room.RoomDatabase
import org.xiaoxigua.xmusic.android.room.dao.PlaylistDao
import org.xiaoxigua.xmusic.android.room.entity.Playlist
import org.xiaoxigua.xmusic.android.room.entity.PlaylistSongCrossRef
import org.xiaoxigua.xmusic.android.room.entity.Song

@Database(entities = [Playlist::class, Song::class, PlaylistSongCrossRef::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun playlistDao(): PlaylistDao
}