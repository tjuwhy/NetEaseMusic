package stanwang.top.musicplayer.auth

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.widget.RemoteViews
import com.google.gson.JsonParseException
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import stanwang.top.musicplayer.R
import stanwang.top.musicplayer.commons.network.HttpClient
import stanwang.top.musicplayer.commons.network.awaitAndHandle
import stanwang.top.musicplayer.commons.preference.Preference
import stanwang.top.musicplayer.music_player.MyLiveData
import stanwang.top.musicplayer.personal_list.PlayListActivity
import stanwang.top.musicplayer.play_list.PlayListDetailActivity

class LoginActivity : AppCompatActivity() {

    val a  = MutableLiveData<Any>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        login_button.setOnClickListener {
            launch(UI) {
                HttpClient.api.login(user_phone.text.toString().trim(),user_pass.text.toString().trim()).awaitAndHandle {
                    Toasty.error(this@LoginActivity,it.message.toString()).show()
                }?.apply {
                    Preference.user = this
                    startActivity(Intent(this@LoginActivity,PlayListActivity::class.java))
                }
            }
        }
    }

}
