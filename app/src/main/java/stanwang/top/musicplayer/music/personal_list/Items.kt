package stanwang.top.musicplayer.personal_list

import android.content.Context
import android.content.Intent
import android.support.v7.app.AlertDialog
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import es.dmoral.toasty.Toasty
import stanwang.top.musicplayer.R
import stanwang.top.musicplayer.auth.User
import stanwang.top.musicplayer.commons.preference.Preference
import stanwang.top.musicplayer.commons.ui.Item
import stanwang.top.musicplayer.commons.ui.ItemController
import stanwang.top.musicplayer.play_list.PlayListDetailActivity

class HeaderItem(val context: Context, val user: User?): Item {
    override val controller: ItemController
        get() = Controller

    companion object Controller:ItemController{


        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val view = inflater.inflate(R.layout.header_play_list,parent,false)
            val shrink = view.findViewById<CardView>(R.id.card_shrink)
            val avatar:ImageView = view.findViewById(R.id.user_avatar)
            val name:TextView = view.findViewById(R.id.user_name)
            val iconShrink = view.findViewById<ImageView>(R.id.icon_shrink)
            val set = view.findViewById<ImageView>(R.id.sec_setting)

            return ViewHolder(view,avatar, name, shrink,iconShrink,set)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as ViewHolder
            item as HeaderItem
            holder.apply {
                Glide.with(item.context)
                        .load(item.user?.profile?.avatarUrl)
                        .into(avatar)
                name.text = item.user?.profile?.nickname
                val builder = AlertDialog.Builder(item.context)

                builder.setTitle("设置单曲播放时长")
                builder.setMessage("请填入数字，单位：秒，若值为-1，代表一直播放直至全曲结束。")
                set.setOnClickListener {
                    val editText = EditText(item.context)
                    editText.hint = "当前时长为${Preference.seconds}"
                    builder.setView(editText)
                    builder.setPositiveButton("确认"){ _, _ ->
                        try {
                            val se = editText.text.toString().toInt()
                            Preference.seconds = se
                        } catch (e:NumberFormatException){
                            Toasty.error(item.context,"请输入数字").show()
                        }
                    }
                    builder.show()

                }
            }
        }
    }

    class ViewHolder(view:View,val avatar:ImageView,val name: TextView,val shrink:CardView, val iconShrink:ImageView,val set:ImageView):RecyclerView.ViewHolder(view)
}

class PlayItem(val context: Context,val playList:Playlist):Item{

    companion object Controller:ItemController{
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as ViewHolder
            item as PlayItem
            holder.apply {
                Glide.with(item.context)
                        .load(item.playList.coverImgUrl)
                        .into(cover)
                name.text = item.playList.name
                num.text = "${item.playList.trackCount} 首"
                itemView.setOnClickListener {
                    val intent = Intent(item.context, PlayListDetailActivity::class.java)
                    intent.putExtra("id",item.playList.id)
                    item.context.startActivity(intent)
                }
            }

        }

        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val view = inflater.inflate(R.layout.item_play_list,parent,false)
            val cover : ImageView = view.findViewById(R.id.list_img)
            val name :TextView = view.findViewById(R.id.list_name)
            val num:TextView = view.findViewById(R.id.list_num)
            val more :ImageView = view.findViewById(R.id.list_more)
            return ViewHolder(view, cover, name, num, more)
        }
    }

    override val controller: ItemController
        get() = Controller

    class ViewHolder(view :View,val cover:ImageView,val name:TextView,val num :TextView,val more:ImageView):RecyclerView.ViewHolder(view)
}