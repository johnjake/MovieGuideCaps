package ph.movieguide.com.features.details

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import ph.movieguide.android_baseplate_persistence.AppDatabase
import ph.movieguide.com.data.mapper.NowPlayingMapper
import ph.movieguide.com.data.mapper.TopRatedMapper
import ph.movieguide.com.data.mapper.VisitedMapper
import ph.movieguide.com.data.vo.MovieScreen
import timber.log.Timber

class Repository(
    private val appDatabase: AppDatabase,
    private val mapper: NowPlayingMapper,
    private val mapperTop: TopRatedMapper,
    private val mapperVisited: VisitedMapper
) : DataSource {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Timber.e("Http Error ###: $throwable")
    }

    override suspend fun getMovieDetailsById(movieId: Int): MovieScreen {
        return coroutineScope.async(exceptionHandler) {
            return@async withContext(Dispatchers.IO) {
                val movieDB = appDatabase.nowPlayingMovieDao().getNowPlayingMovieById(movieId)
                val movieData = mapper.mapFromStorage(movieDB)
                movieData
            }
        }.await()
    }

    override suspend fun getTopRatedMovieById(movieId: Int): MovieScreen {
        return coroutineScope.async(exceptionHandler) {
            return@async withContext(Dispatchers.IO) {
                val movieDB = appDatabase.topRatedMovieDao().getTopRatedMovieById(movieId)
                val movieData = mapperTop.mapFromStorage(movieDB)
                movieData
            }
        }.await()
    }

    /** change to discover **/
    override suspend fun getDiscoverMovieById(movieId: Int): MovieScreen {
        return coroutineScope.async(exceptionHandler) {
            return@async withContext(Dispatchers.IO) {
                val movieDB = appDatabase.topRatedMovieDao().getTopRatedMovieById(movieId)
                val movieData = mapperTop.mapFromStorage(movieDB)
                movieData
            }
        }.await()
    }

    override suspend fun insertMovie(movie: MovieScreen) {
        val dbMap = mapperVisited.mapFromData(movie)
        appDatabase.visitedDao().insertMovieScreen(dbMap)
    }
}