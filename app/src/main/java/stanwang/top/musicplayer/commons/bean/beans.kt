package stanwang.top.musicplayer.commons.bean


data class UserBean(
		val loginType: Int,
		val code: Int,
		val account: Account,
		val profile: Profile,
		val bindings: List<Binding>
)

data class Account(
		val id: Int,
		val userName: String,
		val type: Int,
		val status: Int,
		val whitelistAuthority: Int,
		val createTime: Long,
		val salt: String,
		val tokenVersion: Int,
		val ban: Int,
		val baoyueVersion: Int,
		val donateVersion: Int,
		val vipType: Int,
		val viptypeVersion: Int,
		val anonimousUser: Boolean
)

data class Binding(
		val url: String,
		val expiresIn: Int,
		val bindingTime: Long,
		val refreshTime: Int,
		val tokenJsonStr: String,
		val userId: Int,
		val expired: Boolean,
		val id: Int,
		val type: Int
)

data class Profile(
		val authStatus: Int,
		val experts: Any,
		val backgroundImgId: Long,
		val userType: Int,
		val mutual: Boolean,
		val remarkName: Any,
		val expertTags: Any,
		val nickname: String,
		val birthday: Long,
		val city: Int,
		val backgroundUrl: String,
		val defaultAvatar: Boolean,
		val avatarUrl: String,
		val djStatus: Int,
		val detailDescription: String,
		val followed: Boolean,
		val province: Int,
		val userId: Int,
		val vipType: Int,
		val gender: Int,
		val avatarImgId: Long,
		val accountStatus: Int,
		val avatarImgIdStr: String,
		val backgroundImgIdStr: String,
		val description: String,
		val signature: String,
		val authority: Int,
		val followeds: Int,
		val follows: Int,
		val eventCount: Int,
		val playlistCount: Int,
		val playlistBeSubscribedCount: Int
)
