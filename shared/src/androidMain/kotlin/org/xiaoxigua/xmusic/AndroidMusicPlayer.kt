package org.xiaoxigua.xmusic

class AndroidMusicPlayer(context: Any) : MusicPlayer(
    context
) {
    override val mediaLists: MutableList<PlatformMediaList> = mutableListOf()

    override fun addPlaylist(mediaList: PlatformMediaList) {
        mediaList.init(libVLC)
        mediaLists.add(mediaList)
    }
}