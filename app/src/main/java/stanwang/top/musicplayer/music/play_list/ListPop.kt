package stanwang.top.musicplayer.play_list

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import stanwang.top.musicplayer.R
import stanwang.top.musicplayer.commons.preference.Preference
import stanwang.top.musicplayer.commons.ui.Item
import stanwang.top.musicplayer.commons.ui.BottomPopupWindow
import stanwang.top.musicplayer.commons.ui.withItems

class ListBottomPop(context: Context): BottomPopupWindow(context){

    lateinit var list:List<Item>

    private val matchParent: Int = android.view.ViewGroup.LayoutParams.MATCH_PARENT
    private val wrapContent: Int = android.view.ViewGroup.LayoutParams.WRAP_CONTENT

    override fun createContentView(parent: ViewGroup): View {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.pop_playlist,parent,false).apply {
            layoutParams = FrameLayout.LayoutParams(matchParent, 750).apply {
                gravity = Gravity.TOP
            }
        }
        view.findViewById<RecyclerView>(R.id.rec_pl).apply {
            layoutManager = LinearLayoutManager(context)
            withItems(list)
        }
        view.findViewById<ImageView>(R.id.clear_all).setOnClickListener {
            Preference.playList = mutableListOf()
            dismiss()
        }
        return view
    }

    init {
        isDismissOnClickBack = true
        isDismissOnTouchBackground = true
    }
}