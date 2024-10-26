package org.xiaoxigua.xmusic

class MusicPlayer(context: Any) {
    private val platformLibVLC: PlatformLibVLC = getPlatformLibVLC()
    val vlcPlayer: PlatformVLCPlayer = getPlatformVLCPlayer()
    val mediaList: PlatformMediaList = getPlatformMediaList()

    init {
        val libVLC = platformLibVLC.getLibVLC(context)

        vlcPlayer.init(libVLC)
        mediaList.init(libVLC)
    }

    fun setMedia(index: Int) {
        val media = mediaList.getMedia(index)
        if (media != null)
            vlcPlayer.setMedia(media)
    }

    fun getUri(index: Int): Any? {
        return if (mediaList.mediaList.size > index && index > 0) {
            mediaList.mediaList[index]
        } else null
    }

    fun getProgress(): Progress = vlcPlayer.getProgress()

    fun getMeta(index: Int): AudioMeta? = mediaList.getMediaMetas(index)

    fun next(index: Int): Int {
        val ret = if (index + 1 < mediaList.getLength()) {
            index + 1
        } else {
            0
        }

        setMedia(ret)

        return ret
    }

    fun prev(index: Int): Int {
        val ret = if (index - 1 > 0) {
            index - 1
        } else {
            mediaList.getLength() - 1
        }

        if (ret > 0)
            setMedia(ret)

        return ret
    }
}