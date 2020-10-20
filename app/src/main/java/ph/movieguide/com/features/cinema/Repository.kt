package ph.movieguide.com.features.cinema

import android.app.Application
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import ph.movieguide.android_baseplate_persistence.AppDatabase
import ph.movieguide.android_baseplate_persistence.model.DBMoviesNowPlaying
import ph.movieguide.android_baseplate_persistence.model.DBMoviesTopRated
import ph.movieguide.com.BuildConfig
import ph.movieguide.com.api.ApiServices
import ph.movieguide.com.data.mapper.NowPlayingMapper
import ph.movieguide.com.data.vo.MovieScreen
import ph.movieguide.com.features.cinema.paging.PageDataSource
import ph.movieguide.com.features.cinema.paging.PagingMediator
import timber.log.Timber

class Repository (
    private val api: ApiServices,
    private val appDatabase: AppDatabase,
    private val mapper: NowPlayingMapper,
    private val application: Application

) : DataSource {

    companion object {
        const val DEFAULT_PAGE_INDEX = 1
        const val DEFAULT_PAGE_SIZE = 10
        const val DEFAULT_LANGUAGE = "en-US"
    }

    private val scope = CoroutineScope(Dispatchers.IO)

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Timber.e("Http Error ###: $throwable")
    }

    @ExperimentalPagingApi
    fun getNowPlayingMoviesFlow(pagingConfig: PagingConfig = getDefaultPageConfig()): Flow<PagingData<MovieScreen>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { PageDataSource(api) }
        ).flow
    }

    /**
     * let's define page size, page size is the only required param, rest is optional
     */
    private fun getDefaultPageConfig(): PagingConfig {
        return PagingConfig(pageSize = DEFAULT_PAGE_SIZE, enablePlaceholders = true)
    }

    override fun getTopRatedMoviesFromDB(): Flow<List<DBMoviesTopRated>> {
       return appDatabase.topRatedMovieDao().getTopRatedMovies()
    }

    override suspend fun getMoviesNowPlaying(
        pageNumber: Int
    ): List<MovieScreen> {
        return scope.async(exceptionHandler) {
            return@async withContext(Dispatchers.IO) {
                val apiKey = BuildConfig.API_KEY
                val dbList = api.getNowPlayingMovies(apiKey, "en-US", pageNumber).results
                dbList.forEach { screen ->
                    val dbScreen = mapper.mapFromData(screen)
                    appDatabase.nowPlayingMovieDao().insertMovieScreen(dbScreen)
                }
                dbList
            }
        }.await()
    }

    @ExperimentalPagingApi
    fun letMovieScreenFlowDb(pagingConfig: PagingConfig = getDefaultPageConfig()): Flow<PagingData<DBMoviesNowPlaying>> {
        val pagingSourceFactory = { appDatabase.nowPlayingMovieDao().getNowShowingMoviePaging() }
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = pagingSourceFactory,
            remoteMediator = PagingMediator(api, appDatabase, mapper)
        ).flow
    }

}