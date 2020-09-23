package ph.movieguide.com.data.mapper

import ph.movieguide.android_baseplate_persistence.model.DBMoviesTopRated
import ph.movieguide.com.data.vo.MovieScreen
import ph.movieguide.com.utils.DBTypeConverter

class TopRatedMapper private constructor() {
    private val typeConvert = DBTypeConverter()

    fun mapFromDomain(from: MovieScreen): Map<String, Any> {
        val map = mutableMapOf<String, Any>()
        map[MOVIE_SCREEN_KEY] = ph.movieguide.android_baseplate_persistence.domain.MovieScreen(
            popularity = from.popularity,
            vote_count = from.vote_count,
            video = from.video,
            poster_path = from.poster_path,
            id = from.id,
            adult = from.adult,
            backdrop_path = from.backdrop_path,
            original_language = from.original_language,
            original_title = from.original_title,
            genre_ids = typeConvert.saveList(from.genre_ids),
            title = from.title,
            vote_average = from.vote_average,
            overview = from.overview,
            release_date = from.release_date
        )
        return map
    }

    fun mapFromStorage(from: DBMoviesTopRated): MovieScreen {
        return MovieScreen(
            popularity = from.popularity,
            vote_count = from.vote_count,
            video = from.video,
            poster_path = from.poster_path,
            id = from.idMovie,
            adult = from.adult,
            backdrop_path = from.backdrop_path,
            original_language = from.original_language,
            original_title = from.original_title,
            genre_ids = typeConvert.restoreList(from.genre_ids),
            title = from.title,
            vote_average = from.vote_average,
            overview = from.overview,
            release_date = from.release_date
        )
    }

    fun mapFromData(from: MovieScreen): DBMoviesTopRated {
        return DBMoviesTopRated(
            0,
            popularity = from.popularity,
            vote_count = from.vote_count,
            video = from.video,
            poster_path = from.poster_path,
            idMovie = from.id,
            adult = from.adult,
            backdrop_path = from.backdrop_path,
            original_language = from.original_language,
            original_title = from.original_title,
            genre_ids = typeConvert.saveList(from.genre_ids),
            title = from.title,
            vote_average = from.vote_average,
            overview = from.overview,
            release_date = from.release_date
        )
    }

    companion object {
        const val MOVIE_SCREEN_KEY = "movie_top_rated"

        private var INSTANCE: TopRatedMapper? = null

        fun getInstance(): TopRatedMapper =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: TopRatedMapper().also { INSTANCE = it }
            }
    }
}