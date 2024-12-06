package org.xiaoxigua.xmusic.android.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Song(
    @PrimaryKey(autoGenerate = true) val songId: Long = 0,
)
