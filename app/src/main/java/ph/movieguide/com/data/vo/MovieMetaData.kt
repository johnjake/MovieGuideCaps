package ph.movieguide.com.data.vo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieMetaData(
    val page: Int = 0,
    val total_results: Int = 0,
    val total_pages: Int = 0,
    val results: List<MovieScreen>,
    val dates: Dates? = null
) : Parcelable