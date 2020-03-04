package stanwang.top.musicplayer.music_player

import android.arch.lifecycle.MutableLiveData
import android.widget.RemoteViews
import stanwang.top.musicplayer.R
import stanwang.top.musicplayer.commons.preference.Preference
import stanwang.top.musicplayer.play_list.Track
import java.lang.IllegalArgumentException

object MyLiveData :MutableLiveData<Track>(){

    var isPlaying = false
    var inde: Int = -1

    override fun onActive() {
        super.onActive()
        value = when(Preference.currentIndex){
            in 0 until Preference.playList.size -> {
                Preference.playList[Preference.currentIndex]
            }
            else -> {
                null
            }
        }
    }

    fun setIndex(index:Int){
        if (index in 0 until Preference.playList.size){
//            if (boolean){
                value = Preference.playList[index]
                Preference.currentIndex = index
//            }
            inde = index
        } else {
            throw IllegalArgumentException()
        }
    }



    fun next(){
        if(Preference.playList.isNotEmpty()){
            isPlaying=true
           if (Preference.currentIndex + 1 >= Preference.playList.size) {
               setIndex(0)
           } else {
               setIndex(Preference.currentIndex+1)
           }
        }
    }

    fun previous(){

        if(Preference.playList.isNotEmpty()){
            isPlaying=true
            if (Preference.currentIndex <=0) {
                setIndex(Preference.playList.size-1)
            } else {
                setIndex(Preference.currentIndex-1)
            }
        }
    }
}


