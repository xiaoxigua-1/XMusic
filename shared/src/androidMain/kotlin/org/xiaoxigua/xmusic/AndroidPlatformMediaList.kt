package org.xiaoxigua.xmusic

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

    override fun addMedia(uri: Any) {
        mediaList.add(uri as Uri)
    }

    override fun getMedia(index: Int): MediaData {
        val fd = (libVLC as LibVLC).appContext.contentResolver.openAssetFileDescriptor(
            mediaList[index],
            "r"
        )

        return MediaData(Media(libVLC as LibVLC, fd), fd)
    }

    override fun removeMedia(index: Int) {
        mediaList.removeAt(index)
    }

    override fun clear() {
        mediaList.clear()
    }

    override fun getMediaMetas(): List<AudioMeta> {
        return mediaList.map { uri ->
            val fd = (libVLC as LibVLC).appContext.contentResolver.openAssetFileDescriptor(uri, "r")
            val media = Media(libVLC as LibVLC, fd)

            media.parse()

            val title = media.getMeta(Meta.Title)
            val artist = media.getMeta(Meta.Artist)
            val album = media.getMeta(Meta.Album)
            val artworkURL = media.getMeta(Meta.ArtworkURL)

            media.release()
            fd?.close()

            AudioMeta(title, artist, album, artworkURL)
        }
    }

    override fun getLength(): Int = mediaList.size
}