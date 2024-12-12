package org.xiaoxigua.xmusic.android.core.data

import android.annotation.SuppressLint
import android.content.res.AssetFileDescriptor
import android.net.Uri
import android.provider.OpenableColumns
import org.videolan.libvlc.LibVLC
import org.videolan.libvlc.Media
import org.videolan.libvlc.interfaces.IMedia
import org.xiaoxigua.xmusic.android.room.entity.Song
import org.xiaoxigua.xmusic.android.room.entity.SongType

sealed class MediaData(private val libVLC: LibVLC, open val song: Song) {
    private fun getFileName(uri: Uri): String? {
        var fileName: String? = null
        // 使用 ContentResolver 查詢 URI
        libVLC.appContext.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                // 查找檔案名稱的欄位 (通常是 OpenableColumns.DISPLAY_NAME)
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex != -1) {
                    fileName = cursor.getString(nameIndex)
                }
            }
        }

        if (fileName != null) {
            // 去掉副檔名
            val dotIndex = fileName!!.lastIndexOf('.')
            if (dotIndex != -1) {
                fileName = fileName!!.substring(0, dotIndex)
            }
        }

        return fileName
    }

    fun getMediaMetas(): Song? {
        media.parse()

        val isSupported = media.tracks?.isNotEmpty()

        val result = if (isSupported == true) {
            val title = media.getMeta(IMedia.Meta.Title)
            val artist = media.getMeta(IMedia.Meta.Artist)
            val album = media.getMeta(IMedia.Meta.Album)
            val artworkURL = media.getMeta(IMedia.Meta.ArtworkURL)

            Song(
                title = if (title == "imem://") getFileName(
                    Uri.parse(song.uri)
                ) else title,
                artist = artist, album = album, artworkURL = artworkURL,
                uri = song.uri,
                type = song.type,
                playlistId = song.playlistId
            )
        } else null

        release()

        return result
    }

    abstract fun release()

    abstract val media: Media

    class LocalFile(private val libVLC: LibVLC, override val song: Song) : MediaData(libVLC, song) {
        @SuppressLint("Recycle")
        fun getFdFromUri(): AssetFileDescriptor? {
            return libVLC.appContext.contentResolver.openAssetFileDescriptor(
                Uri.parse(song.uri),
                "r"
            )
        }

        private val fd = getFdFromUri()

        override val media: Media = Media(libVLC, fd)

        override fun release() {
            media.release()
            fd?.close()
        }
    }

    class HTTPStream(libVLC: LibVLC, song: Song) : MediaData(libVLC, song) {
        override val media: Media = Media(libVLC, Uri.parse(song.uri))

        override fun release() {
            media.release()
        }
    }

    companion object {
        fun getMediaData(libVLC: LibVLC, song: Song): MediaData {
            return when (song.type) {
                SongType.Content -> LocalFile(libVLC, song)
                SongType.HTTP -> HTTPStream(libVLC, song)
            }
        }

        fun init(libVLC: LibVLC, playlistId: Long, uri: Uri): MediaData {
            val type = when (uri.scheme) {
                "content" -> SongType.Content
                else -> SongType.HTTP
            }
            val song = Song(
                uri = uri.toString(), type = type, playlistId = playlistId
            )

            return getMediaData(libVLC, song)
        }
    }
}