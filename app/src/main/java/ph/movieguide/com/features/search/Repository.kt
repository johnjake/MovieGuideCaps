package ph.movieguide.com.features.search

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.dsl.module
import ph.movieguide.android_baseplate_persistence.AppDatabase
import ph.movieguide.android_baseplate_persistence.model.DBMoviesNowPlaying
import ph.movieguide.com.BuildConfig
import ph.movieguide.com.api.ApiServices
import ph.movieguide.com.data.mapper.NowPlayingMapper
import ph.movieguide.com.data.vo.MovieScreen
import timber.log.Timber

val searchRepoModule = module {
    factory { Repository(apiServices = get(), app = get(), mapper = get()) }
}
class Repository(
    private val apiServices: ApiServices,
    private val app: AppDatabase,
    private val mapper: NowPlayingMapper
) : DataSource {

    private val scope = CoroutineScope(Dispatchers.IO)

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Timber.e("Http Error ###: $throwable")
    }

    override fun getNowPlayingMovies(): Flow<List<DBMoviesNowPlaying>> {
        return app.nowPlayingMovieDao().getNowPlayingMovies()
    }

    override suspend fun getSearchMovies(
        query: String,
        pageNumber: Int
    ): List<MovieScreen> {
        return scope.async(exceptionHandler) {
            return@async withContext(Dispatchers.IO) {
                val apiKey = BuildConfig.API_KEY
                apiServices.getSearchMovies(apiKey, query, pageNumber).results
            }
        }.await()
    }

    override suspend fun saveDiscoverList(list: List<MovieScreen>) {
        scope.launch {
            list.forEach { screen ->
                val dbScreen = mapper.mapFromData(screen)
                val disCover = mapper.mapDiscover(screen)
                app.nowPlayingMovieDao().insertMovieScreen(dbScreen)
                app.discoverDao().insertMovieScreen(disCover)
            }
        }
    }
}