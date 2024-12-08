package org.xiaoxigua.xmusic.android.room

import androidx.room.TypeConverter
import org.xiaoxigua.xmusic.android.room.entity.SongType

class Converters {
    @TypeConverter
    fun fromSongType(value: SongType): String {
        return value.name
    }

    @TypeConverter
    fun toSongType(value: String): SongType {
        return SongType.valueOf(value)
    }
}