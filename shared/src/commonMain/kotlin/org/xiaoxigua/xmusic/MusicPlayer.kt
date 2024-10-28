package org.xiaoxigua.xmusic

abstract class MusicPlayer(context: Any) {
    private val platformLibVLC: PlatformLibVLC = getPlatformLibVLC()
    protected var libVLC: Any = platformLibVLC.getLibVLC(context)
    val vlcPlayer: PlatformVLCPlayer = getPlatformVLCPlayer()
    abstract val mediaLists: MutableList<PlatformMediaList>

    init {
        vlcPlayer.init(libVLC)
    }

    private fun setMedia(currentPlayList: Int, index: Int) {
        val media = getCurrentPlayList(currentPlayList)?.getMedia(index)
        if (media != null)
            vlcPlayer.setMedia(media)
    }

    fun getCurrentPlayList(currentPlayList: Int): PlatformMediaList? {
        return if (currentPlayList < mediaLists.size && currentPlayList > 0)
            mediaLists[currentPlayList]
        else null
    }

    private fun getCurrentPlaylistLength(currentPlayList: Int) =
        getCurrentPlayList(currentPlayList)?.getLength() ?: 0

    fun getProgress(): Progress = vlcPlayer.getProgress()

    fun getMeta(currentPlayList: Int, index: Int): AudioMeta? =
        getCurrentPlayList(currentPlayList)?.getMediaMetas(index)

    abstract fun addPlaylist(mediaList: PlatformMediaList)

    fun next(currentPlayList: Int, index: Int): Int {
        val ret = if (index + 1 < getCurrentPlaylistLength(currentPlayList)) {
            index + 1
        } else {
            0
        }

        setMedia(currentPlayList, ret)

        return ret
    }

    fun prev(currentPlayList: Int, index: Int): Int {
        val ret = if (index - 1 > 0) {
            index - 1
        } else {
            getCurrentPlaylistLength(currentPlayList) - 1
        }

        if (ret > 0)
            setMedia(currentPlayList, ret)

        return ret
    }
}