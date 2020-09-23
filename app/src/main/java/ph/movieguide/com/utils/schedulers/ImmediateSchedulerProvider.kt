package ph.movieguide.com.utils.schedulers

import io.reactivex.CompletableTransformer
import io.reactivex.MaybeTransformer
import io.reactivex.Scheduler
import io.reactivex.SingleTransformer
import io.reactivex.schedulers.Schedulers

/**
 * Implementation of the [BaseSchedulerProvider] making all [Scheduler]s immediate.
 */
abstract class ImmediateSchedulerProvider : BaseSchedulerProvider {

    override fun computation(): Scheduler {
        return Schedulers.single()
    }

    override fun io(): Scheduler {
        return Schedulers.single()
    }

    override fun ui(): Scheduler {
        return Schedulers.single()
    }

    override fun <T> applySchedulers(): SingleTransformer<T, T> {
        TODO("not implemented")
    }

    override fun <T> applyMaybeSchedulers(): MaybeTransformer<T, T> {
        TODO("not implemented")
    }

    override fun applyCompletableSchedulers(): CompletableTransformer {
        TODO("not implemented")
    }
}