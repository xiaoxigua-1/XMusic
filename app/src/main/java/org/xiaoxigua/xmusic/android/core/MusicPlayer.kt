package org.xiaoxigua.xmusic.android.core

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.videolan.libvlc.LibVLC
import org.videolan.libvlc.MediaPlayer
import org.xiaoxigua.xmusic.android.core.data.MediaData
import org.xiaoxigua.xmusic.android.core.data.Progress

class MusicPlayer(libVLC: LibVLC) : ViewModel() {
    private val mediaPlayer = MediaPlayer(libVLC)
    private var nowMediaData: MediaData? = null
    private var _progress: MutableLiveData<Progress> = MutableLiveData()

    val progress: LiveData<Progress> = _progress

    init {
        mediaPlayer.setEventListener { e ->
            if (e.esChangedType == 0)
                _progress.value = Progress(mediaPlayer.time, e.positionChanged, mediaPlayer.length)
        }
    }

    fun setMedia(mediaData: MediaData) {
        if (mediaPlayer.hasMedia())
            nowMediaData?.release()

        nowMediaData = mediaData
        mediaPlayer.media = mediaData.media
    }

    fun play() {
        mediaPlayer.media?.let {
            mediaPlayer.play()
        }
    }

    fun pause() {
        if (mediaPlayer.isPlaying)
            mediaPlayer.pause()
    }

    fun setPos(pos: Float) {
        mediaPlayer.pause()
        mediaPlayer.position = pos
        mediaPlayer.play()
    }
}