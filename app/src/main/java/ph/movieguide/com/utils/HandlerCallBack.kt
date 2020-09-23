package ph.movieguide.com.utils

import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class HandlerCallBack {

    interface CallBack<T> {
        fun onComplete(result: T)
        fun onException(e: Exception?)
    }

    suspend fun <T> awaitCallback(block: (CallBack<T>) -> Unit): T =
        suspendCancellableCoroutine { cont ->
            block(object : CallBack<T> {
                override fun onComplete(result: T) = cont.resume(result)
                override fun onException(e: Exception?) {
                    e?.let { cont.resumeWithException(it) }
                }
            })
        }
}