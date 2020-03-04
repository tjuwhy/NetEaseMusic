package stanwang.top.musicplayer.music_player

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.util.Log
import stanwang.top.musicplayer.commons.preference.Preference

class MusicPlayService : Service() {

    private val myBinder = MyBinder()
    private var startId: Int = 0

    inner class MyBinder:Binder(){
        fun getService() = this@MusicPlayService
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val i = Intent(this, MusicPlayerActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this,0,i,0)
        val notify = Notification.Builder(this).setTicker("Service").setContentIntent(pendingIntent).build()
        startForeground(startId, notify)
        val bundles = intent.extras
        this.startId = startId
        Log.d("cache",Preference.currentIndex.toString())
        bundles?.apply {
            val control = getSerializable("key") as Control?
            when(control){
                Control.PLAY -> {
                    val url = getString("url")
                    play(url)
                }
                Control.PAUSE ->{
                    if (mediaPlayer.isPlaying) {
                        mediaPlayer.pause()
                    } else {
                        mediaPlayer.start()
                    }
                }
                else -> {}
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder {
        return myBinder
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.apply {
            stop()
            release()
        }
    }

    val mediaPlayer=MediaPlayer()

    private fun play(url:String){
        if(mediaPlayer.isPlaying){
            mediaPlayer.stop()
        }
        mediaPlayer.reset()
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepare()
        mediaPlayer.start()
    }

}



