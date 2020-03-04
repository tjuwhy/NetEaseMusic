package stanwang.top.musicplayer.music_player

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.*
import android.arch.lifecycle.Observer
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v7.app.AppCompatActivity
import android.view.animation.LinearInterpolator
import android.widget.SeekBar
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import es.dmoral.toasty.Toasty
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.activity_music_player.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import stanwang.top.musicplayer.R
import stanwang.top.musicplayer.commons.network.HttpClient
import stanwang.top.musicplayer.commons.network.awaitAndHandle
import stanwang.top.musicplayer.commons.preference.Preference
import stanwang.top.musicplayer.play_list.PlayListDetailActivity

class MusicPlayerActivity : AppCompatActivity() {

    private lateinit var animator: ObjectAnimator
    var ms: MusicPlayService? = null
    val handler = Handler()

    val sc = object :ServiceConnection{

        override fun onServiceDisconnected(name: ComponentName?) {
            ms = null
        }

        @RequiresApi(Build.VERSION_CODES.N)
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            ms = (service as MusicPlayService.MyBinder).getService()
            startPlaying()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun startPlaying(){
        handler.post(MyRunnable())
        seekBar.max = ms!!.mediaPlayer.duration
        maxTime.text = SimpleDateFormat("mm:ss").format(ms!!.mediaPlayer.duration)
        MyLiveData.observe(this, Observer {
            seekBar.max = ms!!.mediaPlayer.duration
            maxTime.text = SimpleDateFormat("mm:ss").format(ms!!.mediaPlayer.duration)
        })
    }

    inner class MyRunnable:Runnable{
        @RequiresApi(Build.VERSION_CODES.N)
        override fun run() {
            seekBar.max = ms!!.mediaPlayer.duration
            maxTime.text = SimpleDateFormat("mm:ss").format(ms!!.mediaPlayer.duration)
            currentProgress.text = SimpleDateFormat("mm:ss").format(ms!!.mediaPlayer.currentPosition)
            seekBar.progress = ms!!.mediaPlayer.currentPosition
            if(Preference.seconds == -1){
                handler.postDelayed(this,99)
                return
            }
            if(ms!!.mediaPlayer.currentPosition >= Preference.seconds * 1000 ){
                MyLiveData.next()
                MyLiveData.value?.apply {
                    val intent = Intent(this@MusicPlayerActivity, MusicPlayService::class.java)
                    val bundle = Bundle()
                    bundle.putSerializable("key", Control.PLAY)
                    launch(UI) {
                        HttpClient.api.getSongUrl(this@apply.id.toString()).awaitAndHandle {
                            Toasty.error(this@MusicPlayerActivity, it.message.toString()).show()
                        }?.apply {
                            bundle.putString("url", data[0].url)
                            intent.putExtras(bundle)
                            MyLiveData.isPlaying = true
                            startService(intent)
                        }
                    }
                }
            }
            handler.postDelayed(this,99)
        }

    }

    lateinit var seekBar:SeekBar
    lateinit var currentProgress:TextView
    lateinit var maxTime:TextView

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_player)
        animator = ObjectAnimator.ofFloat(rotating_cover, "rotation", 0f, 360.0f)
        animator.duration = 20000
        animator.interpolator = LinearInterpolator()//匀速
        animator.repeatCount = -1//设置动画重复次数（-1代表一直转）
        animator.repeatMode = ValueAnimator.RESTART//动画重复模式
        seekBar = seek_bar
        currentProgress = current_time
        maxTime = total_time
        serviceConnection()
        seek_bar.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener{
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser){
                    ms!!.mediaPlayer.seekTo(progress)
                }
            }
        })
        icon_arrow_back_m.setOnClickListener {
            onBackPressed()
        }
        music_previous.setOnClickListener {
            MyLiveData.previous()
            MyLiveData.value?.apply {

                val intent = Intent(this@MusicPlayerActivity, MusicPlayService::class.java)
                val bundle = Bundle()
                bundle.putSerializable("key", Control.PLAY)
                launch(UI) {
                    HttpClient.api.getSongUrl(this@apply.id.toString()).awaitAndHandle {
                        Toasty.error(this@MusicPlayerActivity, it.message.toString()).show()
                    }?.apply {
                        bundle.putString("url", data[0].url)
                        intent.putExtras(bundle)
                        MyLiveData.isPlaying = true
                        startService(intent)
                    }
                }
            }
        }
        music_next.setOnClickListener {
            MyLiveData.next()
            MyLiveData.value?.apply {

                val intent = Intent(this@MusicPlayerActivity, MusicPlayService::class.java)
                val bundle = Bundle()
                bundle.putSerializable("key", Control.PLAY)
                launch(UI) {
                    HttpClient.api.getSongUrl(this@apply.id.toString()).awaitAndHandle {
                        Toasty.error(this@MusicPlayerActivity, it.message.toString()).show()
                    }?.apply {
                        bundle.putString("url", data[0].url)
                        intent.putExtras(bundle)
                        MyLiveData.isPlaying = true
                        startService(intent)
                    }
                }
            }
        }
        music_play_pause.setOnClickListener {
            if (MyLiveData.isPlaying) {
                animator.pause()
                music_play_pause.setImageResource(R.drawable.play)
            } else {
                animator.resume()
                music_play_pause.setImageResource(R.drawable.pause)
            }
            MyLiveData.isPlaying = !MyLiveData.isPlaying
            val intent = Intent(this@MusicPlayerActivity, MusicPlayService::class.java)
            val bundle = Bundle()
            bundle.putSerializable("key", Control.PAUSE)
            intent.putExtras(bundle)
            startService(intent)
        }
        MyLiveData.observe(this, Observer {
            if(MyLiveData.isPlaying){
                music_play_pause.setImageResource(R.drawable.pause)
                animator.start()
            } else {
                music_play_pause.setImageResource(R.drawable.play)
            }
            it?.apply {
                music_name.text = it.name
                music_author.text= it.ar[0].name + " >"
                Glide.with(this@MusicPlayerActivity)
                        .load(it.al.picUrl)
                        .apply(RequestOptions.placeholderOf(R.drawable.sky))
                        .apply(RequestOptions.bitmapTransform(BlurTransformation(4,25)))
                        .into(music_player_background)
                Glide.with(this@MusicPlayerActivity)
                        .load(it.al.picUrl)
                        .apply(RequestOptions.placeholderOf(R.drawable.default_cover))
                        .into(rotating_cover)

            }
        })
    }

    private fun serviceConnection(){
        val intent = Intent(this,MusicPlayService::class.java)
        bindService(intent,sc, Service.BIND_AUTO_CREATE)
    }
}
