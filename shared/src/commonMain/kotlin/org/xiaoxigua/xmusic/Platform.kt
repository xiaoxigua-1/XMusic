package org.xiaoxigua.xmusic

interface PlatformVLCPlayer {
    val isPlaying: Boolean
    val isSeekable: Boolean

    fun init(libVlc: Any)

    fun setMedia(media: MediaData)

    fun play(): Boolean

    fun pause(): Boolean

    fun stop(): Boolean

    fun setPosition(pos: Float)

    fun setUpdate(f: (Progress) -> Unit, onFinished: () -> Unit)

    fun getProgress(): Progress
}

interface PlatformLibVLC {
    fun getLibVLC(context: Any): Any
}

interface PlatformMediaList {
    val libVLC: Any
    val mediaList: List<Any>

    fun init(libVlc: Any)

    /**
     * @param[uri] media uri
     * @return Return whether this URI format is supported.
     */
    fun addMedia(uri: Any): Boolean

    fun getMedia(index: Int): MediaData?

    fun removeMedia(index: Int)

    fun clear()

    fun getMediaMetas(index: Int): AudioMeta?

    fun getLength(): Int
}

expect fun getPlatformLibVLC(): PlatformLibVLC

expect fun getPlatformVLCPlayer(): PlatformVLCPlayer

expect fun getPlatformMediaList(): PlatformMediaList