package ph.movieguide.com.features.main

import io.reactivex.Single
import kotlinx.coroutines.flow.Flow
import ph.movieguide.android_baseplate_persistence.model.DBMovieScreen
import ph.movieguide.com.data.vo.MovieDetails
import ph.movieguide.com.data.vo.MovieScreen

interface DataSource {
    fun getMovieDetails(movieId: Int): Single<MovieDetails>
    suspend fun getDetailsMovie(movieCode: Int): MovieDetails
    suspend fun getDiscoverMovieList(pageNumber: Int): List<MovieScreen>
    suspend fun getSearchMovieList(searchName: String, pageNumber: Int): List<MovieScreen>
    suspend fun getMovieScreenDBList(): Flow<List<DBMovieScreen>>
    suspend fun getMovieScreenDBId(movieCode: Int): MovieScreen
}