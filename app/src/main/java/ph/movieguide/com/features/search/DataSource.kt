package ph.movieguide.com.features.search

import kotlinx.coroutines.flow.Flow
import ph.movieguide.android_baseplate_persistence.model.DBMoviesNowPlaying
import ph.movieguide.com.data.vo.MovieScreen

interface DataSource {
    fun getNowPlayingMovies(): Flow<List<DBMoviesNowPlaying>>
    suspend fun getSearchMovies(query: String, pageNumber: Int): List<MovieScreen>
    suspend fun saveDiscoverList(list: List<MovieScreen>)
}