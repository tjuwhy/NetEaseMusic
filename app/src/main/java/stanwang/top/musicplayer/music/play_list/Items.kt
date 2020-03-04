package stanwang.top.musicplayer.play_list

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.Image
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import es.dmoral.toasty.Toasty
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import stanwang.top.musicplayer.R
import stanwang.top.musicplayer.commons.network.HttpClient
import stanwang.top.musicplayer.commons.network.awaitAndHandle
import stanwang.top.musicplayer.commons.preference.Preference
import stanwang.top.musicplayer.commons.ui.Item
import stanwang.top.musicplayer.commons.ui.ItemController
import stanwang.top.musicplayer.music_player.Control
import stanwang.top.musicplayer.music_player.MusicPlayService
import stanwang.top.musicplayer.music_player.MyLiveData

class PlayListHeader(val context:Context,val playListDetail: PlayListDetail): Item {

    class ViewHolder(itemview:View,val coverBg:ImageView,val cover:ImageView,val arr_back:ImageView,val uname:TextView,val avatar:ImageView,val name:TextView):RecyclerView.ViewHolder(itemview)

    companion object Controller:ItemController{
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val view = inflater.inflate(R.layout.header_play_list_detail,parent,false)
            val coverBg = view.findViewById<ImageView>(R.id.play_list_cover_bg)
            val cover =  view.findViewById<ImageView>(R.id.play_list_cover)
            val arr = view.findViewById<ImageView>(R.id.icon_arrow_back)
            val uname = view.findViewById<TextView>(R.id.play_list_username)
            val avatar = view.findViewById<ImageView>(R.id.play_list_avatar)
            val name = view.findViewById<TextView>(R.id.play_list_name)
            return ViewHolder(view,coverBg,cover,arr,uname,avatar, name)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as ViewHolder
            item as PlayListHeader
            item.apply {
                Glide.with(context)
                        .load(playListDetail.playlist.coverImgUrl)
                        .apply(RequestOptions.placeholderOf(R.drawable.sky))
                        .apply(RequestOptions.centerCropTransform())
                        .apply(RequestOptions.bitmapTransform(BlurTransformation(4,25)))
                        .into(holder.coverBg)
                Glide.with(context)
                        .load(playListDetail.playlist.coverImgUrl)
                        .apply(RequestOptions.placeholderOf(R.drawable.default_cover))
                        .into(holder.cover)
                Glide.with(context)
                        .load(playListDetail.playlist.creator.avatarUrl)
                        .apply(RequestOptions.placeholderOf(R.drawable.default_avatar))
                        .into(holder.avatar)
                holder.uname.text = "${playListDetail.playlist.creator.nickname} >"
                holder.name.text = playListDetail.playlist.name

                holder.arr_back.setOnClickListener {
                    (context as Activity).onBackPressed()
                }
            }
        }
    }

    override val controller: ItemController
        get() = Controller
}

class SongItem(val context: Context,val num :Int,val track: Track):Item{

    companion object Controller:ItemController{
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val view= inflater.inflate(R.layout.item_play_list_detail,parent,false)
            val num = view.findViewById<TextView>(R.id.song_num)
//            val avatar = view.findViewById<ImageView>(R.id.song_avatar)
            val name = view.findViewById<TextView>(R.id.song_name)
            val author = view.findViewById<TextView>(R.id.song_author)
            return ViewHolder(view,num,  name, author)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            item as SongItem
            holder as ViewHolder
            holder.apply {
                num.text = item.num.toString()
//                Glide.with(item.context)
//                        .load(item.track.al.picUrl)
//                        .into(avatar)
                name.text = item.track.name
                var str = ""
                val size = item.track.ar.size-1
                for (i in 0 until size){
                    str += item.track.ar[i].name + "/"
                }
                str += item.track.ar[size].name
                author.text = "$str - ${item.track.al.name}"
                itemView.setOnClickListener {
                    val intent = Intent(item.context,MusicPlayService::class.java)
                    val bundle = Bundle()
                    bundle.putSerializable("key",Control.PLAY)
                    launch(UI) {
                        HttpClient.api.getSongUrl(item.track.id.toString()).awaitAndHandle{
                            Toasty.error(item.context,it.message.toString()).show()
                        }?.apply {
                            bundle.putString("url",data[0].url)
                            intent.putExtras(bundle)
                            val list = Preference.playList
                            list.remove(item.track)
                            list.add(0,item.track)
                            MyLiveData.isPlaying = true
                            Preference.playList = list
                            MyLiveData.setIndex(0)
                            item.context.startService(intent)
                        }
                    }
                }
            }
        }
    }

    class ViewHolder(itemView:View,val num:TextView,val name: TextView,val author:TextView):RecyclerView.ViewHolder(itemView)

    override val controller: ItemController
        get() = Controller
}

class ListShowItem(val context: Context,val track: Track,val pop: ListBottomPop,val index:Int):Item{

    class ViewHolder(itemView: View,val name:TextView,val author: TextView,val delete:ImageView):RecyclerView.ViewHolder(itemView)

    companion object Controller:ItemController{
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pl,parent,false)
            val name = view.findViewById<TextView>(R.id.pl_name)
            val author = view.findViewById<TextView>(R.id.pl_author)
            val delete = view.findViewById<ImageView>(R.id.delete)
            return ViewHolder(view,name, author,delete)
        }


        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as ViewHolder
            item as ListShowItem
            if (item.track == MyLiveData.value){
                holder.name.setTextColor(Color.parseColor("#ea2000"))
                holder.author.setTextColor(Color.parseColor("#ea2000"))
            }
            holder.name.text = item.track.name
            holder.author.text = " - ${item.track.ar[0].name}"
            holder.delete.setOnClickListener {
                Preference.playList.remove(item.track)
                item.pop.dismiss()
            }
            holder.itemView.setOnClickListener {
                val intent = Intent(item.context,MusicPlayService::class.java)
                val bundle = Bundle()
                bundle.putSerializable("key",Control.PLAY)
                launch(UI) {
                    HttpClient.api.getSongUrl(item.track.id.toString()).awaitAndHandle{
                        Toasty.error(item.context,it.message.toString()).show()
                    }?.apply {
                        bundle.putString("url",data[0].url)
                        intent.putExtras(bundle)
                        MyLiveData.isPlaying = true
                        item.context.startService(intent)
                    }
                }
                MyLiveData.setIndex(item.index)
                item.pop.dismiss()
            }
        }
    }

    override val controller: ItemController
        get() = Controller

}
