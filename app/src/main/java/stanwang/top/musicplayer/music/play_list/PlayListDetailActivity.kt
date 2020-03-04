package stanwang.top.musicplayer.play_list

import android.view.View
import android.arch.lifecycle.Observer
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_play_list_detail.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import stanwang.top.musicplayer.R
import stanwang.top.musicplayer.commons.network.HttpClient
import stanwang.top.musicplayer.commons.network.awaitAndHandle
import stanwang.top.musicplayer.commons.preference.Preference
import stanwang.top.musicplayer.commons.ui.Item
import stanwang.top.musicplayer.commons.ui.withItems
import stanwang.top.musicplayer.music_player.Control
import stanwang.top.musicplayer.music_player.MusicPlayService
import stanwang.top.musicplayer.music_player.MusicPlayerActivity
import stanwang.top.musicplayer.music_player.MyLiveData
import stanwang.top.musicplayer.personal_list.HeaderItem

class PlayListDetailActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_list_detail)
        val id = intent.getStringExtra("id")
        rec_play_list_detail.layoutManager = LinearLayoutManager(this)
        launch(UI) {
            HttpClient.api.getPlayListDetail(id).awaitAndHandle {
                Toasty.error(this@PlayListDetailActivity, it.message.toString()).show()
            }?.apply {
                val list = mutableListOf<Item>(PlayListHeader(this@PlayListDetailActivity, this))
                playlist.tracks.forEachIndexed { i, it ->
                    list.add(SongItem(this@PlayListDetailActivity, i + 1, it))
                }
                rec_play_list_detail.withItems(list)
            }
        }
        song_next.setOnClickListener {
            MyLiveData.next()
            MyLiveData.value.apply {
                val intent = Intent(this@PlayListDetailActivity, MusicPlayService::class.java)
                val bundle = Bundle()
                bundle.putSerializable("key", Control.PLAY)
                launch(UI) {
                    HttpClient.api.getSongUrl(id.toString()).awaitAndHandle {
                        Toasty.error(this@PlayListDetailActivity, it.message.toString()).show()
                    }?.apply {
                        bundle.putString("url", data[0].url)
                        intent.putExtras(bundle)
                        MyLiveData.isPlaying = true
                        startService(intent)
                    }
                }

            }
        }
        song_prev.setOnClickListener {
            MyLiveData.previous()
            MyLiveData.value.apply {
                val intent = Intent(this@PlayListDetailActivity, MusicPlayService::class.java)
                val bundle = Bundle()
                bundle.putSerializable("key", Control.PLAY)
                launch(UI) {
                    HttpClient.api.getSongUrl(id.toString()).awaitAndHandle {
                        Toasty.error(this@PlayListDetailActivity, it.message.toString()).show()
                    }?.apply {
                        bundle.putString("url", data[0].url)
                        intent.putExtras(bundle)
                        MyLiveData.isPlaying = true
                        startService(intent)
                    }
                }
            }
        }
        song_play_pause.setOnClickListener {
            if (MyLiveData.isPlaying) {
                song_play_pause.setImageResource(R.drawable.ic_play_circle_outline_black_24dp)
            } else {
                song_play_pause.setImageResource(R.drawable.ic_pause_circle_outline_black_24dp)
            }
            MyLiveData.isPlaying = !MyLiveData.isPlaying

            val intent = Intent(this@PlayListDetailActivity, MusicPlayService::class.java)
            val bundle = Bundle()
            bundle.putSerializable("key", Control.PAUSE)
            intent.putExtras(bundle)
            startService(intent)
        }
        song_bottom_bar.setOnClickListener {
            startActivity(Intent(this,MusicPlayerActivity::class.java))
        }
        MyLiveData.observe(this@PlayListDetailActivity, Observer<Track> { track ->
//            flag = true
            if (track == null) {
                song_bottom_bar.visibility = View.GONE
            } else {
                song_bottom_bar.visibility = View.VISIBLE
                Glide.with(this@PlayListDetailActivity)
                        .load(track.al.picUrl)
                        .into(song_img)
                song_n.text = track.name
                if (!MyLiveData.isPlaying) {
                    song_play_pause.setImageResource(R.drawable.ic_play_circle_outline_black_24dp)
                } else {
                    song_play_pause.setImageResource(R.drawable.ic_pause_circle_outline_black_24dp)
                }

            }
        })
        song_playlist.setOnClickListener {
//            Toasty.normal(this,"111").show()
            val pop = ListBottomPop(this)
            val list = mutableListOf<Item>()
            Preference.playList.mapIndexed{index,it ->
                list.add(ListShowItem(this@PlayListDetailActivity,it,pop,index))
            }
            pop.list = list
            pop.show()
        }
    }
}