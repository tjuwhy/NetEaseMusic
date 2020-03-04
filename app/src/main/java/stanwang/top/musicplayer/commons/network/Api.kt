package stanwang.top.musicplayer.commons.network

import kotlinx.coroutines.experimental.Deferred
import retrofit2.http.GET
import retrofit2.http.Query
import stanwang.top.musicplayer.auth.User
import stanwang.top.musicplayer.music_player.UrlBean
import stanwang.top.musicplayer.personal_list.Play
import stanwang.top.musicplayer.play_list.PlayListDetail

interface Api{

    @GET("/login/cellphone")
    fun login(@Query("phone") phone:String,@Query("password") password:String):Deferred<User>

    @GET("/user/playlist")
    fun getPlay(@Query("uid") uid:String) : Deferred<Play>

    @GET("/playlist/detail")
    fun getPlayListDetail(@Query("id") id:String) : Deferred<PlayListDetail>

    @GET("/song/url")
    fun getSongUrl(@Query("id") id:String) : Deferred<UrlBean>
}