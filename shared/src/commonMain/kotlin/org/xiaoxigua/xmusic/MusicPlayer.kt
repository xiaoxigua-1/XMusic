package org.xiaoxigua.xmusic

class MusicPlayer(context: Any, private var current: Int = 0) {
    private val platformLibVLC: PlatformLibVLC = getPlatformLibVLC()
    val vlcPlayer: PlatformVLCPlayer = getPlatformVLCPlayer()
    val mediaList: PlatformMediaList = getPlatformMediaList()

    init {
        val libVLC = platformLibVLC.getLibVLC(context)

        vlcPlayer.init(libVLC)
        mediaList.init(libVLC)
    }

    fun setCurrentMedia() {
        vlcPlayer.setMedia(mediaList.getMedia(current))
    }

    fun getCurrentUri() = mediaList.mediaList[current]

    fun getProgress(): Progress = vlcPlayer.getProgress()

    fun currentMeta(): AudioMeta? = mediaList.getMediaMetas().getOrNull(current)

    fun next() {
        if (current + 1 < mediaList.getLength()) {
            current++
        } else {
            current = 0
        }

        setCurrentMedia()
    }

    fun prev() {
        if (current - 1 > 0) {
            current--
        } else {
            current = mediaList.getLength() - 1
        }

        setCurrentMedia()
    }
}