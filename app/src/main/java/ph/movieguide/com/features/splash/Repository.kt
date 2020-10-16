package ph.movieguide.com.features.splash

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import ph.movieguide.android_baseplate_persistence.AppDatabase
import ph.movieguide.com.BuildConfig
import ph.movieguide.com.api.ApiServices
import ph.movieguide.com.data.mapper.TopRatedMapper
import ph.movieguide.com.data.vo.MovieScreen
import timber.log.Timber

open class Repository (
    private val api: ApiServices,
    private val appDatabase: AppDatabase,
    private val mapper: TopRatedMapper
) : DataSource {

    private val scope = CoroutineScope(Dispatchers.IO)

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Timber.e("Http Error ###: $throwable")
    }

    override suspend fun getTopRatedMovies(
        languages: String,
        pageNumber: Int
    ): List<MovieScreen> {
        return scope.async(exceptionHandler) {
            return@async withContext(Dispatchers.IO) {
                val apiKey = BuildConfig.API_KEY
                val dbList = api.getTopRatedMovies(apiKey, languages, pageNumber).results
                dbList.forEach { screen ->
                    val dbScreen = mapper.mapFromData(screen)
                    appDatabase.topRatedMovieDao().insertMovieScreen(dbScreen)
                }
                api.getTopRatedMovies(apiKey, languages, pageNumber).results
            }
        }.await()
    }
}