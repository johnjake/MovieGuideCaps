package ph.movieguide.com.ext

import android.content.Context
import android.telephony.TelephonyManager
import ph.movieguide.com.BuildConfig
import java.text.SimpleDateFormat
import java.util.*

fun Long.convertTimeToString(formatter: SimpleDateFormat): String {
    return formatter.format(Date(this))
}

fun Context.getNetworkCountryIso(): String {
    when (BuildConfig.BUILD_TYPE) {
        "debug" -> return "PH"
        "staging" -> return "AU"
        "release" -> return "AU"
        "prod" -> return "AU"
    }
    val tm = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    return tm.networkCountryIso.toUpperCase()
}

fun String.pluralize(count: Int, plural: String?): String? {
    return if (count > 1) {
        plural ?: this + 's'
    } else {
        this
    }
}

fun <T: Comparable<T>> ArrayList<T>.equalLists(other: ArrayList<T>?): Boolean {
    if (other == null) {
        return true
    }

    if ( size != other.size ) {
        return false
    }

    val thisCopy = ArrayList(this)
    val otherCopy = ArrayList(other)

    thisCopy.sort()
    otherCopy.sort()


    return thisCopy == otherCopy
}