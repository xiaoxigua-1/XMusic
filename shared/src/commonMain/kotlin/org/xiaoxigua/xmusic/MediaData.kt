package org.xiaoxigua.xmusic

sealed class MediaData {
    abstract val media: Any?

    open fun release() {}

    open class LocalFile(override val media: Any?) : MediaData()

    open class HTTPStream(override val media: Any?) : MediaData()
}
