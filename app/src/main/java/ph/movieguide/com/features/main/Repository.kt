package ph.movieguide.com.features.main

import io.reactivex.Single
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import org.koin.dsl.module
import ph.movieguide.android_baseplate_persistence.AppDatabase
import ph.movieguide.android_baseplate_persistence.model.DBMovieScreen
import ph.movieguide.com.BuildConfig
import ph.movieguide.com.api.ApiServices
import ph.movieguide.com.data.mapper.MovieMapper
import ph.movieguide.com.data.vo.MovieDetails
import ph.movieguide.com.data.vo.MovieScreen
import timber.log.Timber

val mainRepoModule = module {
    factory { Repository(api = get(), appDatabase = get(), mapper = get()) }
}
class Repository(
    private val api: ApiServices,
    private val appDatabase: AppDatabase,
    private val mapper: MovieMapper
) : DataSource {

    private val scope = CoroutineScope(Dispatchers.IO)

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Timber.e("Http Error ###: $throwable")
    }

    override fun getMovieDetails(movieId: Int): Single<MovieDetails> {
        val apiKey = BuildConfig.API_KEY
        return api.movieDetails(apiKey)
    }

    override suspend fun getDetailsMovie(movieCode: Int): MovieDetails {
        return scope.async(exceptionHandler) {
            return@async withContext(Dispatchers.IO) {
                val apiKey = BuildConfig.API_KEY
                api.getMovieDetails(movieCode, apiKey)
            }
        }.await()
    }

    override suspend fun getDiscoverMovieList(pageNumber: Int): List<MovieScreen> {
        return scope.async(exceptionHandler) {
            return@async withContext(Dispatchers.IO) {
                val apiKey = BuildConfig.API_KEY
                val dbList = api.getDiscoverMovies(apiKey, pageNumber).results
                dbList.forEach { screen ->
                    val dbScreen = mapper.mapFromData(screen)
                    appDatabase.movieScreenDao().insertMovieScreen(dbScreen)
                }
               return@withContext dbList
            }
        }.await()
    }

    override suspend fun getSearchMovieList(
        searchName: String,
        pageNumber: Int
    ): List<MovieScreen> {
        return scope.async(exceptionHandler) {
            return@async withContext(Dispatchers.IO) {
                val apiKey = BuildConfig.API_KEY
                val dbList = api.getSearchMovies(apiKey, searchName, pageNumber).results
                dbList.forEach { screen ->
                   val dbScreen = mapper.mapFromData(screen)
                   appDatabase.movieScreenDao().insertMovieScreen(dbScreen)
                }
                api.getSearchMovies(apiKey, searchName, pageNumber).results
            }
        }.await()
    }

    override suspend fun getMovieScreenDBList(): Flow<List<DBMovieScreen>> {
        return appDatabase
            .movieScreenDao()
            .getMovieScreen()
    }

    override suspend fun getMovieScreenDBId(movieCode: Int): MovieScreen {
        TODO("Not yet implemented")
    }
}