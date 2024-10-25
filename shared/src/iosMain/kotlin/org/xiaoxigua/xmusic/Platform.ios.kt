package org.xiaoxigua.xmusic

class IOSPlatform : PlatformVLCPlayer {
    override fun play() {
        TODO("Not yet implemented")
    }

    override fun pause() {
        TODO("Not yet implemented")
    }

    override fun getMetas() {
        TODO("Not yet implemented")
    }
}

actual fun getPlatformVLCPlayer(): PlatformVLCPlayer = IOSPlatform()
actual fun getPlatformMediaList(): PlatformMediaList {
    TODO("Not yet implemented")
}

actual fun getPlatformLibVLC(): PlatformLibVLC {
    TODO("Not yet implemented")
}