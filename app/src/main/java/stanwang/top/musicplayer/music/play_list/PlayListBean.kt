package stanwang.top.musicplayer.play_list


data class PlayListDetail(
		val playlist: Playlist,
		val code: Int,
		val privileges: List<Privilege>
)

data class Privilege(
		val id: Int,
		val fee: Int,
		val payed: Int,
		val st: Int,
		val pl: Int,
		val dl: Int,
		val sp: Int,
		val cp: Int,
		val subp: Int,
		val cs: Boolean,
		val maxbr: Int,
		val fl: Int,
		val toast: Boolean,
		val flag: Int,
		val preSell: Boolean
)

data class Playlist(
		val subscribers: List<Any>,
		val subscribed: Boolean,
		val creator: Creator,
		val tracks: List<Track>,
		val trackIds: List<TrackId>,
		val cloudTrackCount: Int,
		val subscribedCount: Int,
		val createTime: Long,
		val highQuality: Boolean,
		val newImported: Boolean,
		val userId: Int,
		val coverImgId: Long,
		val updateTime: Long,
		val specialType: Int,
		val trackCount: Int,
		val commentThreadId: String,
		val trackUpdateTime: Long,
		val privacy: Int,
		val playCount: Int,
		val coverImgUrl: String,
		val adType: Int,
		val trackNumberUpdateTime: Long,
		val ordered: Boolean,
		val tags: List<Any>,
		val description: Any,
		val status: Int,
		val name: String,
		val id: String,
		val shareCount: Int,
		val commentCount: Int
)

data class TrackId(
		val id: Int,
		val v: Int
)

data class Creator(
		val defaultAvatar: Boolean,
		val province: Int,
		val authStatus: Int,
		val followed: Boolean,
		val avatarUrl: String,
		val accountStatus: Int,
		val gender: Int,
		val city: Int,
		val birthday: Long,
		val userId: Int,
		val userType: Int,
		val nickname: String,
		val signature: String,
		val description: String,
		val detailDescription: String,
		val avatarImgId: Long,
		val backgroundImgId: Long,
		val backgroundUrl: String,
		val authority: Int,
		val mutual: Boolean,
		val expertTags: Any,
		val experts: Any,
		val djStatus: Int,
		val vipType: Int,
		val remarkName: Any,
		val avatarImgIdStr: String,
		val backgroundImgIdStr: String,
		val avatarImgId_str: String
)

data class Track(
		val name: String,
		val id: Int,
		val pst: Int,
		val t: Int,
		val ar: List<Ar>,
		val alia: List<Any>,
		val pop: Int,
		val st: Int,
		val rt: String,
		val fee: Int,
		val v: Int,
		val crbt: Any,
		val cf: String,
		val al: Al,
		val dt: Int,
		val h: H,
		val m: M,
		val l: L,
		val a: Any,
		val cd: String,
		val no: Int,
		val rtUrl: Any,
		val ftype: Int,
		val rtUrls: List<Any>,
		val djId: Int,
		val copyright: Int,
		val s_id: Int,
		val rtype: Int,
		val rurl: Any,
		val mst: Int,
		val cp: Int,
		val mv: Int,
		val publishTime: Long
)

data class Al(
		val id: Int,
		val name: String,
		val picUrl: String,
		val tns: List<Any>,
		val pic: Long
)

data class Ar(
		val id: Int,
		val 	name: String,
		val tns: List<Any>,
		val alias: List<Any>
)

data class H(
		val br: Int,
		val fid: Int,
		val size: Int,
		val vd: String
)

data class L(
		val br: Int,
		val fid: Int,
		val size: Int,
		val vd: String
)

data class M(
		val br: Int,
		val fid: Int,
		val size: Int,
		val vd: String
)