package ph.movieguide.android_baseplate_persistence.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = DBMoviesTopRated.TABLE_NAME_MOVIE_TOP)
data class DBMoviesTopRated(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val popularity: Double = 0.0,
    val vote_count: Int = 0,
    val video: Boolean = false,
    val poster_path: String? = "",
    val idMovie: Int = 0,
    val adult: Boolean = false,
    val backdrop_path: String? = "",
    val original_language: String? = "",
    val original_title: String? = "",
    val genre_ids: String? = "",
    val title: String? = "",
    val vote_average: Double = 0.0,
    val overview: String? = "",
    val release_date: String = ""
){
    companion object {
        const val TABLE_NAME_MOVIE_TOP = "movie_top"
    }

    constructor(): this(
        0,
        0.0,
        0,
        false, "",
        0,
        false,
        "",
        "",
        "",
        "",
        "",
        0.0,
        "",
        ""
    )
}