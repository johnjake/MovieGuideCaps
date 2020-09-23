package ph.movieguide.com.data.vo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieScreen(
    val popularity: Double = 0.0,
    val vote_count: Int = 0,
    val video: Boolean = false,
    val poster_path: String? = "",
    val id: Int = 0,
    val adult: Boolean = false,
    val backdrop_path: String? = "",
    val original_language: String? = "",
    val original_title: String? = "",
    val genre_ids: List<Int> = emptyList(),
    val title: String? = "",
    val vote_average: Double = 0.0,
    val overview: String? = "",
    val release_date: String = ""
) : Parcelable