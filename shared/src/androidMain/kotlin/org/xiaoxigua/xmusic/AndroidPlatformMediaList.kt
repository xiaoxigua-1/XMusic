package org.xiaoxigua.xmusic

import android.content.res.AssetFileDescriptor
import android.net.Uri
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

    private fun checkFileFormat(fd: AssetFileDescriptor?): Boolean {
        val media: Media? = Media(libVLC as LibVLC, fd)

        media?.parse()

        val isSupported = media?.tracks?.isNotEmpty()

        media?.release()
        fd?.close()

        return isSupported == true
    }

    override fun addMedia(uri: Any) {
        val fd = getFdFromUri(uri as Uri)

        if (checkFileFormat(fd)) {
            mediaList.add(uri)
        }
    }

    override fun getMedia(index: Int): MediaData? {
        return mediaList.getOrNull(index)?.let { uri ->
            val fd = getFdFromUri(uri)

            MediaData(Media(libVLC as LibVLC, fd), fd)
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
            val fd = getFdFromUri(uri)
            val media = Media(libVLC as LibVLC, fd)

            media.parse()

            val title = media.getMeta(Meta.Title)
            val artist = media.getMeta(Meta.Artist)
            val album = media.getMeta(Meta.Album)
            val artworkURL = media.getMeta(Meta.ArtworkURL)

            media.release()
            fd?.close()

            AudioMeta(if (title == "imem://") null else title, artist, album, artworkURL)
        }
    }

    override fun getLength(): Int = mediaList.size
}