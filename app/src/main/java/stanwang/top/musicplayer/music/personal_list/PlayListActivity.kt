package stanwang.top.musicplayer.personal_list

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_play_list.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import stanwang.top.musicplayer.R
import stanwang.top.musicplayer.commons.network.HttpClient
import stanwang.top.musicplayer.commons.network.awaitAndHandle
import stanwang.top.musicplayer.commons.preference.Preference
import stanwang.top.musicplayer.commons.ui.Item
import stanwang.top.musicplayer.commons.ui.ItemAdapter
import stanwang.top.musicplayer.commons.ui.ItemManager
import stanwang.top.musicplayer.commons.ui.withItems

class PlayListActivity : AppCompatActivity(){

    val list = mutableListOf<Item>()
    lateinit var itemManager : ItemManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_list)
        rec_play_list.layoutManager = LinearLayoutManager(this)
        itemManager = ItemManager()
        rec_play_list.adapter = ItemAdapter(itemManager)
        launch(UI) {
            HttpClient.api.getPlay(Preference.user?.account?.id.toString()).awaitAndHandle{
                Toasty.error(this@PlayListActivity,it.message.toString()).show()
                Log.e("play",it.message)
                rec_play_list.withItems(mutableListOf(HeaderItem(this@PlayListActivity,Preference.user)))
            }?.apply {
                this.playlist.mapTo(list){
                    PlayItem(this@PlayListActivity,it)
                }
                list.add(0,HeaderItem(this@PlayListActivity,Preference.user))
                rec_play_list.withItems(list)
            }
        }
    }

    override fun onBackPressed() {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("真的要退出吗？")
        alertDialog.setPositiveButton("确定"){_,_ ->
            finish()
        }
        alertDialog.setNegativeButton("取消",null)
        alertDialog.show()
    }
}
