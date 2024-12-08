package org.xiaoxigua.xmusic.android.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.xiaoxigua.xmusic.android.room.dao.PlaylistDao
import org.xiaoxigua.xmusic.android.room.entity.Playlist
import org.xiaoxigua.xmusic.android.room.entity.PlaylistSongCrossRef
import org.xiaoxigua.xmusic.android.room.entity.Song

@Database(entities = [Playlist::class, Song::class, PlaylistSongCrossRef::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun playlistDao(): PlaylistDao
}