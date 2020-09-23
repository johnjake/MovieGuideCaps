package ph.movieguide.com.features.cinema

import kotlinx.coroutines.flow.Flow
import ph.movieguide.android_baseplate_persistence.model.DBMoviesTopRated
import ph.movieguide.com.data.vo.MovieScreen

interface DataSource {

    fun getTopRatedMoviesFromDB(): Flow<List<DBMoviesTopRated>>

    suspend fun getMoviesNowPlaying(pageNumber: Int): List<MovieScreen>
}