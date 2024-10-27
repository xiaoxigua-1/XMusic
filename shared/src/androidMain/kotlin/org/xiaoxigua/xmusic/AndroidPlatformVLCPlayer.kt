package org.xiaoxigua.xmusic

import org.videolan.libvlc.LibVLC
import org.videolan.libvlc.Media
import org.videolan.libvlc.MediaPlayer

class AndroidPlatformVLCPlayer : PlatformVLCPlayer {
    private lateinit var libVLC: LibVLC
    private lateinit var mediaPlayer: MediaPlayer
    private var media: MediaData? = null
    override val isPlaying: Boolean
        get() = mediaPlayer.isPlaying
    override val isSeekable: Boolean
        get() = mediaPlayer.isSeekable

    override fun init(libVlc: Any) {
        libVLC = libVlc as LibVLC
        mediaPlayer = MediaPlayer(libVLC)
    }

    override fun setMedia(media: MediaData) {
        if (mediaPlayer.hasMedia()) {
            this.media?.release()
        }
        this.media = media
        mediaPlayer.media = media.media as Media
    }

    override fun play(): Boolean {
        return mediaPlayer.media?.let {
            mediaPlayer.play()
        } != null
    }

    override fun stop(): Boolean {
        mediaPlayer.stop()

        return true
    }

    override fun pause(): Boolean {
        mediaPlayer.pause()

        return true
    }

    override fun setPosition(pos: Float) {
        mediaPlayer.position = pos
    }

    override fun setUpdate(f: (Progress) -> Unit, onFinished: () -> Unit) {
        mediaPlayer.setEventListener { e ->
            if (e.esChangedType == 0)
                f(Progress(mediaPlayer.time, e.positionChanged, mediaPlayer.length))
            if (e.type == MediaPlayer.Event.EndReached) {
                onFinished()
            }
        }
    }

    override fun getProgress(): Progress =
        Progress(mediaPlayer.time, mediaPlayer.position, mediaPlayer.length)
}