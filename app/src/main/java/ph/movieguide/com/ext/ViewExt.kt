package ph.movieguide.com.ext

import android.content.Context
import android.graphics.Rect
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.view.touches
import io.reactivex.BackpressureStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber
import java.util.concurrent.TimeUnit

fun View.setBackground(@DrawableRes id: Int) {
    this.background = ContextCompat.getDrawable(this.context, id)
}

fun View.getViewY(root: ViewGroup): Int {
    val offsetViewBounds = Rect()
    getDrawingRect(offsetViewBounds)
    root.offsetDescendantRectToMyCoords(this, offsetViewBounds)
    return offsetViewBounds.top
}

fun View.doubleClick(onComplete: () -> Unit): Disposable {
    val timeout = 400L
    val timeUnits = TimeUnit.MILLISECONDS
    val observable = this.clicks().share().toFlowable(BackpressureStrategy.LATEST)

    return observable.buffer(observable.debounce(timeout, timeUnits))
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeBy(onNext = {
            if (it.size == 2) onComplete.invoke()
        }, onError = {
            Timber.e("Error double click $it")
        })
}

fun View.doubleTouch(onComplete: () -> Unit): Disposable {
    val timeout = 400L
    val timeUnits = TimeUnit.MILLISECONDS
    val observable = this.touches()
        .filter {
            it.action == MotionEvent.ACTION_UP
        }.share().toFlowable(BackpressureStrategy.LATEST)
    return observable.buffer(observable.debounce(timeout, timeUnits))
        .subscribeOn(AndroidSchedulers.mainThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeBy(onNext = {
            if (it.size == 2) onComplete.invoke()
        }, onError = {
            Timber.e("Error dooubleTouch")
        })
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.disable() {
    isEnabled = false
}

fun View.enable() {
    isEnabled = true
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.hide() {
    visibility = View.GONE
}

fun View.setTopMargin(margin: Int) {
    (layoutParams as ViewGroup.MarginLayoutParams).topMargin = margin
}

fun View.setHeight(newHeight: Int) {
    layoutParams.height = newHeight
}

fun View.getMarginLayoutParam() = layoutParams as ViewGroup.MarginLayoutParams

fun View.hideKeyboard(context: Context?) {
    let { v ->
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(v.windowToken, 0)
    }
}

const val NINJA_TAP_THROTTLE_TIME = 400L

fun View.ninjaTap(onNext: (View) -> Unit): Disposable {
    return this.clicks().throttleFirst(NINJA_TAP_THROTTLE_TIME, TimeUnit.MILLISECONDS)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeBy(
            onNext = {
                onNext.invoke(this)
            }
        )
}