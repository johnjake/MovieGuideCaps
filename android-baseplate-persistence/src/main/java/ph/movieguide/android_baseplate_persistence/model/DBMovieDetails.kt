package ph.movieguide.android_baseplate_persistence.model

import androidx.room.Entity

@Entity(tableName = DBMovieDetails.TABLE_NAME_MOVIES)
data class DBMovieDetails(
    val adult: Boolean = false,
    val backdrop_path: String? = "",
    val belongs_to_collection: String? = "",
    val budget: Int = 0,
    val genres: String? = "",
    val homepage: String? = "",
    val id: Int = 0,
    val imdb_id: String? = "",
    val original_language: String? = "",
    val original_title: String? = "",
    val overview: String? = "",
    val popularity: Double = 0.0,
    val poster_path: String? = "",
    val production_companies: String? = "",
    val production_countries: String = "",
    val release_date: String? = "",
    val revenue: Long = 0L,
    val runtime: Int = 0,
    val spoken_languages: String? = "",
    val status: String? = "",
    val tagline: String? = "",
    val title: String? = "",
    val video: Boolean = false,
    val vote_average: Double = 0.0,
    val vote_count: Int = 0
) {
    companion object {
        const val TABLE_NAME_MOVIES = "movie_details"
    }

    constructor() : this(false,
        "",
        "",
        0,
        "",
        "",
        0,
        "",
        "",
        "",
        "",
        0.0,
        "",
        "",
        "",
        "",
        0L,
        0,
        "",
        "",
        "",
        "",
        false,
        0.0,
        0 )
}