package org.xiaoxigua.xmusic

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.net.Uri
import android.provider.OpenableColumns
import org.videolan.libvlc.LibVLC
import org.videolan.libvlc.Media
import org.videolan.libvlc.interfaces.IMedia.Meta

class AndroidPlatformMediaList : PlatformMediaList {
    override lateinit var libVLC: Any
    override val mediaList = mutableListOf<Uri>()

    override fun init(libVlc: Any) {
        libVLC = libVlc
    }

    private fun getFdFromUri(uri: Uri): AssetFileDescriptor? {
        return (libVLC as LibVLC).appContext.contentResolver.openAssetFileDescriptor(
            uri,
            "r"
        )
    }

    private fun checkIsSupport(mediaData: MediaData?): Boolean {
        val media: Media? = (mediaData?.media as Media?)
        media?.parse()

        val isSupported = media?.tracks?.isNotEmpty()

        mediaData?.release()

        return isSupported == true
    }

    private fun getMediaDataFromUri(uri: Uri): MediaData? {
        // TODO: support http and more

        return when (uri.scheme) {
            // android local file
            "content" -> {
                val fd = getFdFromUri(uri)
                // The Media could potentially be null
                val media = Media(libVLC as LibVLC, fd) as Media?

                object : MediaData.LocalFile(media) {
                    override fun release() {
                        (this.media as Media?)?.release()
                        fd?.close()
                    }
                }
            }

            else -> null
        }
    }

    private fun getFileName(context: Context, uri: Uri?): String? {
        var fileName: String? = null
        // 使用 ContentResolver 查詢 URI
        if (uri != null) {
            context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    // 查找檔案名稱的欄位 (通常是 OpenableColumns.DISPLAY_NAME)
                    val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (nameIndex != -1) {
                        fileName = cursor.getString(nameIndex)
                    }
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

    override fun addMedia(uri: Any): Boolean {
        val mediaData = getMediaDataFromUri(uri as Uri)

        return if (checkIsSupport(mediaData)) {
            mediaList.add(uri)
        } else false
    }

    override fun getMedia(index: Int): MediaData? {
        return mediaList.getOrNull(index)?.let { uri ->
            getMediaDataFromUri(uri)
        }
    }

    override fun removeMedia(index: Int) {
        mediaList.removeAt(index)
    }

    override fun clear() {
        mediaList.clear()
    }

    override fun getMediaMetas(index: Int): AudioMeta? {
        return mediaList.getOrNull(index)?.let { uri ->
            val mediaData = getMediaDataFromUri(uri)
            val context = (libVLC as LibVLC).appContext
            val media = (mediaData?.media as Media?)

            media?.parse()

            val title = media?.getMeta(Meta.Title)
            val artist = media?.getMeta(Meta.Artist)
            val album = media?.getMeta(Meta.Album)
            val artworkURL = media?.getMeta(Meta.ArtworkURL)

            mediaData?.release()

            AudioMeta(
                if (title == "imem://") getFileName(context, uri) else title,
                artist,
                album,
                artworkURL
            )
        }
    }

    override fun getLength(): Int = mediaList.size
}