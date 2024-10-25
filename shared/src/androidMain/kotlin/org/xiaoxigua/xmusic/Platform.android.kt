package org.xiaoxigua.xmusic

actual fun getPlatformMediaList(): PlatformMediaList = AndroidPlatformMediaList()
actual fun getPlatformVLCPlayer(): PlatformVLCPlayer = AndroidPlatformVLCPlayer()
actual fun getPlatformLibVLC(): PlatformLibVLC = AndroidPlatformLibVLC()