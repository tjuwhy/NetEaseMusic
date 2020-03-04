package stanwang.top.musicplayer.commons.preference

import stanwang.top.musicplayer.auth.User
import stanwang.top.musicplayer.play_list.Track

object Preference {

    var user: User? by hawk("user", null)

    var currentIndex: Int by hawk("track", 0)

    var playList: MutableList<Track> by hawk("track_list", mutableListOf())

    var seconds: Int by hawk("sec", -1)
}