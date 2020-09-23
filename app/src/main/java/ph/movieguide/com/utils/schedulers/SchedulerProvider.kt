package ph.movieguide.com.utils.schedulers

import io.reactivex.CompletableTransformer
import io.reactivex.MaybeTransformer
import io.reactivex.Scheduler
import io.reactivex.SingleTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Provides different types of schedulers.
 */
class SchedulerProvider // Prevent direct instantiation.
private constructor() : BaseSchedulerProvider {

    override fun computation(): Scheduler {
        return Schedulers.computation()
    }

    override fun io(): Scheduler {
        return Schedulers.io()
    }

    override fun ui(): Scheduler {
        return AndroidSchedulers.mainThread()
    }

    override fun <T> applySchedulers(): SingleTransformer<T, T> = SingleTransformer {
        it.subscribeOn(io()).observeOn(ui())
    }

    override fun <T> applyMaybeSchedulers(): MaybeTransformer<T, T> = MaybeTransformer {
        it.subscribeOn(io()).observeOn(ui())
    }

    override fun applyCompletableSchedulers(): CompletableTransformer = CompletableTransformer {
        it.subscribeOn(io()).observeOn(ui())
    }

    companion object {

        private var INSTANCE: SchedulerProvider? = null

        fun getInstance(): SchedulerProvider =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: SchedulerProvider().also { INSTANCE = it }
            }
    }
}