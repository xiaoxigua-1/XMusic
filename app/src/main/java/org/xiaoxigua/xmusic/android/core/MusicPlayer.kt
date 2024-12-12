package org.xiaoxigua.xmusic.android.core

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.videolan.libvlc.LibVLC
import org.videolan.libvlc.MediaPlayer
import org.xiaoxigua.xmusic.android.core.data.MediaData
import org.xiaoxigua.xmusic.android.core.data.PlaylistData
import org.xiaoxigua.xmusic.android.core.data.Progress

class MusicPlayer(private val libVLC: LibVLC) : ViewModel() {
    companion object {
        enum class PlayerLoopMode {
            PlaylistLoop,
            SingLoop,
            None
        }
    }

    private val mediaPlayer = MediaPlayer(libVLC)
    private var nowMediaData: MediaData? = null
    private var _progress: MutableLiveData<Progress> = MutableLiveData()
    private var _nowPlaylist: MutableLiveData<PlaylistData> = MutableLiveData()
    private var _isPlaying: MutableLiveData<Boolean> = MutableLiveData(mediaPlayer.isPlaying)
    private var _playerLoopMode: MutableLiveData<PlayerLoopMode> = MutableLiveData(PlayerLoopMode.None)
    private var _playerRandomMode: MutableLiveData<Boolean> = MutableLiveData(false)

    val progress: LiveData<Progress> = _progress
    val isPlaying: LiveData<Boolean> = _isPlaying
    val nowPlaylist: LiveData<PlaylistData> = _nowPlaylist
    val playerLoopMode: LiveData<PlayerLoopMode> = _playerLoopMode
    val playerRandomMode: LiveData<Boolean> = _playerRandomMode

    init {
        mediaPlayer.setEventListener { e ->
            if (e.esChangedType == 0)
                _progress.value = Progress(mediaPlayer.time, e.positionChanged, mediaPlayer.length)
            if (e.type == MediaPlayer.Event.EndReached) {
                next()
            }
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
            _isPlaying.value = true
        }
    }

    fun pause() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            _isPlaying.value = false
        }
    }

    fun next() {
        if (_nowPlaylist.value?.index?.value!! < _nowPlaylist.value?.songs?.size!!) {
            val prvIndex = _nowPlaylist.value?.index?.value ?: 0
            val nowSong = _nowPlaylist.value?.songs!![prvIndex + 1]

            _nowPlaylist.value?.index?.value = prvIndex + 1
            setMedia(MediaData.getMediaData(libVLC, nowSong))
            play()
        }
    }

    fun setPos(pos: Float) {
        mediaPlayer.pause()
        mediaPlayer.position = pos
        mediaPlayer.play()
    }

    fun setLoopType(type: PlayerLoopMode) {
        _playerLoopMode.value = type
    }

    fun setPlaylist(playlistData: PlaylistData) {
        mediaPlayer.pause()
        setMedia(MediaData.getMediaData(libVLC,
            playlistData.songs[playlistData.index.value!!]
        ))
        play()
        _nowPlaylist.value = playlistData
    }
}