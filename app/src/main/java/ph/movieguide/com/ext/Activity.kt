package ph.movieguide.com.ext

import android.app.Activity
import android.content.res.Resources
import android.os.Build
import android.os.Environment
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import java.io.File
import java.io.IOException

fun Activity.setContentBehindStatusBar() {
    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
}

fun Activity.setStatusBarTranslucent(statusBar: View, color: Int) {
    if (color == 0) {
        return
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        statusBar.setBackgroundColor(
            if (color > 0) ContextCompat.getColor(
                this,
                color
            ) else color
        )

        statusBar.viewTreeObserver
            .addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    statusBar.viewTreeObserver.removeOnPreDrawListener(this)
                    val statusBarHeight = getStatusBarHeight()
                    val params = statusBar.layoutParams as ViewGroup.LayoutParams
                    params.height = statusBarHeight
                    statusBar.layoutParams = params
                    return true
                }
            })
    }
}

fun getStatusBarHeight(): Int {
    val res = Resources.getSystem()
    val resourceId = res.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        return res.getDimensionPixelSize(resourceId)
    }

    return 0
}

fun Activity.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

@Throws(IOException::class)
fun Activity.createImageFile(): File {
    val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!

    return File.createTempFile(
        "avatar", /* prefix */
        ".jpg", /* suffix */
        storageDir /* directory */
    )
}