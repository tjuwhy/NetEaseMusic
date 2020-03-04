package stanwang.top.musicplayer.music_player


data class UrlBean(
		val data: List<Data>,
		val code: Int
)

data class Data(
		val id: Int,
		val url: String,
		val br: Int,
		val size: Int,
		val md5: String,
		val code: Int,
		val expi: Int,
		val type: String,
		val gain: Double,
		val fee: Int,
		val uf: Any,
		val payed: Int,
		val flag: Int,
		val canExtend: Boolean,
		val freeTrialInfo: Any,
		val level: String,
		val encodeType: String
)