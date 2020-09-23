package ph.movieguide.com.features.main

import io.reactivex.Single
import kotlinx.coroutines.flow.Flow
import org.koin.dsl.module
import ph.movieguide.android_baseplate_persistence.model.DBMovieScreen
import ph.movieguide.com.data.vo.MovieDetails
import ph.movieguide.com.data.vo.MovieScreen
import ph.movieguide.com.interactors.MovieDetailsInteractor
val mainDataSourceModule = module {
    factory { MovieDetailsImpl(repository = get()) }
}
class MovieDetailsImpl (
    private val repository: Repository
) : MovieDetailsInteractor {

    override fun getMovieDetails(movieId: Int): Single<MovieDetails> = repository.getMovieDetails(movieId)

    override suspend fun getMovieDetail(movieId: Int): MovieDetails = repository.getDetailsMovie(movieId)

    override suspend fun getDiscoverMovieList(pageNumber: Int): List<MovieScreen> = repository.getDiscoverMovieList(pageNumber)

    override suspend fun getSearchMovieList(
        searchName: String,
        pageNumber: Int
    ): List<MovieScreen> = repository.getSearchMovieList(searchName, pageNumber)

    override suspend fun getMovieScreenDBList(): Flow<List<DBMovieScreen>> = repository.getMovieScreenDBList()
}