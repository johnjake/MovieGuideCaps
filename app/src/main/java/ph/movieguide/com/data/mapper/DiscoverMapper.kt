package ph.movieguide.com.data.mapper

import ph.movieguide.android_baseplate_persistence.model.DBMovieDiscover
import ph.movieguide.com.data.vo.Discover
import ph.movieguide.com.data.vo.MovieScreen
import ph.movieguide.com.utils.DBTypeConverter
import ph.movieguide.android_baseplate_persistence.domain.MovieScreen as DBScreen

class DiscoverMapper private constructor() {
    private val typeConvert = DBTypeConverter()

    fun mapFromDomain(from: MovieScreen): Map<String, Any> {
        val map = mutableMapOf<String, Any>()
        map[MOVIE_SCREEN_KEY] = DBScreen(
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

    fun mapFromStorage(from: DBMovieDiscover): MovieScreen {
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

    fun mapFromData(from: MovieScreen): DBMovieDiscover {
        return DBMovieDiscover(
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

    fun mapFromParcelizeToSerialized(from: MovieScreen) : Discover {
        return Discover(popularity = from.popularity,
            vote_count = from.vote_count,
            video = from.video,
            poster_path = from.poster_path,
            id = from.id,
            adult = from.adult,
            backdrop_path = from.backdrop_path,
            original_language = from.original_language,
            original_title = from.original_title,
            genre_ids = from.genre_ids,
            title = from.title,
            vote_average = from.vote_average,
            overview = from.overview,
            release_date = from.release_date)
    }

    companion object {
        const val MOVIE_SCREEN_KEY = "movie_now_playing"

        private var INSTANCE: DiscoverMapper? = null

        fun getInstance(): DiscoverMapper =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: DiscoverMapper().also { INSTANCE = it }
            }
    }
}