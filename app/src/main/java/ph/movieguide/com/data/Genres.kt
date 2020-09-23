package ph.movieguide.com.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Genres(
    val id : Int = 0,
    val name : String? = ""
) : Parcelable