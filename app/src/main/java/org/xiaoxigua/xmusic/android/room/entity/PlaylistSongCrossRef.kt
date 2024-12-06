package org.xiaoxigua.xmusic.android.room.entity

import androidx.room.Entity
import androidx.room.Index

@Entity(primaryKeys = ["playlistId", "songId"], indices = [Index(value = ["songId"])])
data class PlaylistSongCrossRef(
    val playlistId: Long,
    val songId: Long
)
