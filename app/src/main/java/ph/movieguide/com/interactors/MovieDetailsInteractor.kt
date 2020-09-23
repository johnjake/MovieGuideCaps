package ph.movieguide.com.interactors

import io.reactivex.Single
import kotlinx.coroutines.flow.Flow
import ph.movieguide.android_baseplate_persistence.model.DBMovieScreen
import ph.movieguide.com.data.vo.MovieDetails
import ph.movieguide.com.data.vo.MovieScreen

interface MovieDetailsInteractor {
    fun getMovieDetails(movieId: Int): Single<MovieDetails>
    suspend fun getMovieDetail(movieId: Int): MovieDetails
    suspend fun getDiscoverMovieList(pageNumber: Int): List<MovieScreen>
    suspend fun getSearchMovieList(searchName: String, pageNumber: Int): List<MovieScreen>
    suspend fun getMovieScreenDBList(): Flow<List<DBMovieScreen>>
}