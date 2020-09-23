package ph.movieguide.com.data.vo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import ph.movieguide.com.data.Genres

@Parcelize
data class MovieDetails(
    val adult: Boolean = false,
    val backdrop_path: String? = "",
    val belongs_to_collection: String? = "",
    val budget: Int = 0,
    val genres: List<Genres>,
    val homepage: String? = "",
    val id: Int = 0,
    val imdb_id: String? = "",
    val original_language: String? = "",
    val original_title: String? = "",
    val overview: String? = "",
    val popularity: Double = 0.0,
    val poster_path: String? = "",
    val production_companies: List<ProductionCompanies> = emptyList(),
    val production_countries: List<ProductionCountries> = emptyList(),
    val release_date: String? = "",
    val revenue: Long = 0L,
    val runtime: Int = 0,
    val spoken_languages: List<SpokenLanguages> = emptyList(),
    val status: String? = "",
    val tagline: String? = "",
    val title: String? = "",
    val video: Boolean = false,
    val vote_average: Double = 0.0,
    val vote_count: Int = 0
) : Parcelable