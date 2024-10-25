package org.xiaoxigua.xmusic

import android.content.Context
import org.videolan.libvlc.LibVLC

class AndroidPlatformLibVLC : PlatformLibVLC {
    override fun getLibVLC(context: Any): Any = LibVLC(context as Context, listOf())
}