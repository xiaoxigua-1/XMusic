package org.xiaoxigua.xmusic.android.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Song(
    @PrimaryKey(autoGenerate = true) val songId: Long = 0,
    @ColumnInfo val uri: String,
    @ColumnInfo val type: SongType,
    @ColumnInfo val title: String? = null,
    @ColumnInfo val artist: String? = null,
    @ColumnInfo val album: String? = null,
    @ColumnInfo val artworkURL: String? = null,
)

enum class SongType {
    Content,
    HTTP
}