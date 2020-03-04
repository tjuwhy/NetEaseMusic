package stanwang.top.musicplayer.commons.network

import kotlinx.coroutines.experimental.CoroutineExceptionHandler
import kotlinx.coroutines.experimental.Deferred

suspend fun <T> Deferred<T>.awaitAndHandle(handler: suspend (Throwable) -> Unit = {}): T? =
        try {
            await()
        } catch (t: Throwable) {
            handler(t)
            null
        }

val QuietCoroutineExceptionHandler = CoroutineExceptionHandler { _, t -> t.printStackTrace() }
