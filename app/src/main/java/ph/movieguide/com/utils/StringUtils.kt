package ph.movieguide.com.utils

import android.annotation.SuppressLint
import ph.movieguide.com.ext.pluralize
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class StringUtils {

    companion object {
        @SuppressLint("SimpleDateFormat")
        @JvmStatic
        fun createFilename(): String {
            return SimpleDateFormat("yyyyMMddHHmm").format(Date())
        }

        @JvmStatic
        fun mmssFormat(count: Int): String {
            val mm = DecimalFormat("00").format(count / 60)
            val ss = DecimalFormat("00").format(count % 60)
            return "$mm:$ss"
        }

        @JvmStatic
        fun millisToLocalDate(millis: Long, pattern: String): String {
            val instant = Instant.ofEpochMilli(millis)
            val zonedDateTime = instant.atZone(ZoneId.systemDefault())
            val dtf = DateTimeFormatter.ofPattern(pattern)
            return dtf.format(zonedDateTime)
        }

        @JvmStatic
        fun daysAgo(dateInMillis: Long): String {
            val now = LocalDate.now()
            val localDate = Instant.ofEpochMilli(dateInMillis).atZone(ZoneId.systemDefault()).toLocalDate()
            val days = Duration.between(localDate.atStartOfDay(), now.atStartOfDay()).toDays()
            val pluralizeDay = "day".pluralize(days.toInt(), null)
            return when (days) {
                1L -> "yesterday"
                7L -> "a week ago"
                14L -> "2 weeks ago"
                21L -> "3 weeks ago"
                else -> "$days $pluralizeDay ago"
            }
        }
    }
}