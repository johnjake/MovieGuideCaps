package ph.movieguide.com.features.cinema.paging

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.koin.dsl.module
import ph.movieguide.android_baseplate_persistence.AppDatabase
import ph.movieguide.android_baseplate_persistence.model.DBMoviesNowPlaying
import ph.movieguide.android_baseplate_persistence.model.DBRemoteKeys
import ph.movieguide.com.api.ApiServices
import ph.movieguide.com.data.mapper.NowPlayingMapper
import ph.movieguide.com.features.cinema.Repository.Companion.DEFAULT_PAGE_INDEX
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import java.io.InvalidObjectException
import androidx.room.withTransaction
import ph.movieguide.com.BuildConfig.API_KEY
import ph.movieguide.com.BuildConfig.DEFAULT_LANGUAGE
import ph.movieguide.com.di.providesApplicationContext
import java.lang.Exception

@ExperimentalPagingApi
val mediatorModule = module {
    factory { PagingMediator(apiServices = get(), appDatabase = get(), mapper = get(), context = get()) }
}

@ExperimentalPagingApi
class PagingMediator(
    private val apiServices: ApiServices,
    private val appDatabase: AppDatabase,
    private val mapper: NowPlayingMapper,
    private val context: Context
) : RemoteMediator<Int, DBMoviesNowPlaying>() {

    /** for single request scope **/
    private val jobCoroutine = Job()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Timber.e("Error Occurred at: $throwable")
    }

    //lifecycle https://medium.com/@marco_cattaneo/kotlin-coroutine-job-lifecycle-c1166039d906
    private val coroutineScope = CoroutineScope(Dispatchers.IO + jobCoroutine + exceptionHandler)

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, DBMoviesNowPlaying>
    ): MediatorResult {
        val page = when (val pageKeyData = getKeyPageData(loadType, state)) {
            is MediatorResult.Success -> {
                return pageKeyData
            }
            else -> {
                if(pageKeyData!= null)
                  pageKeyData as Int
                else 0
            }
        }

        try {
            val apiKey = API_KEY
            val language = DEFAULT_LANGUAGE
            val response = apiServices.getNowPlayingMovies(apiKey, language, page)
            val isEndOfList = response.results.isEmpty()
            appDatabase.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    appDatabase.remoteKeysDao().clearRemoteKeys()
                    appDatabase.nowPlayingMovieDao().clearAllNowPlayingMovie()
                }
                val prevKey = if (page == DEFAULT_PAGE_INDEX) null else page - 1
                val nextKey = if (isEndOfList) null else page + 1
                val dbKeys = response.results.map {
                    DBRemoteKeys(repoId = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                appDatabase.remoteKeysDao().insertAll(dbKeys)
                response.results.forEach { movie ->
                    val domainMovie = mapper.mapFromData(movie)
                    appDatabase.nowPlayingMovieDao().insertMovieScreen(domainMovie)
                }
            }
            return MediatorResult.Success(endOfPaginationReached = isEndOfList)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }
    /**
     * this returns the page key or the final end of list success result
     */
    @SuppressLint("LogNotTimber")
    private suspend fun getKeyPageData(loadType: LoadType, state: PagingState<Int, DBMoviesNowPlaying>): Any? {
        return when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getClosestRemoteKey(state)
                remoteKeys?.nextKey?.minus(1) ?: DEFAULT_PAGE_INDEX
            }
            LoadType.APPEND -> {
                try {
                    val remoteKeys = getLastRemoteKey(state) ?: null
                    remoteKeys?.nextKey
                }catch (ex: Exception) {
                    Log.e("PREPEND", "message: ${ex.message}")
                }
            }
            LoadType.PREPEND -> {
                try {
                    val remoteKeys = getFirstRemoteKey(state) ?: null
                    //end of list condition reached
                    remoteKeys?.prevKey
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                    remoteKeys.prevKey
                } catch (ex: Exception) {
                    Log.e("PREPEND", "message: ${ex.message}")
                }
            }
        }
    }
    /**
     * get the last remote key inserted which had the data
     */
    private suspend fun getLastRemoteKey(state: PagingState<Int, DBMoviesNowPlaying>): DBRemoteKeys? {
        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { movie -> appDatabase.remoteKeysDao().remoteKeysMovieId(movie.idMovie) }
    }
    /**
     * get the first remote key inserted which had the data
     */
    private suspend fun getFirstRemoteKey(state: PagingState<Int, DBMoviesNowPlaying>): DBRemoteKeys? {
        return state.pages
            .firstOrNull() { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { movie -> appDatabase.remoteKeysDao().remoteKeysMovieId(movie.idMovie) }
    }
    /**
     * get the closest remote key inserted which had the data
     */
    private suspend fun getClosestRemoteKey(state: PagingState<Int, DBMoviesNowPlaying>): DBRemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { repoId ->
                appDatabase.remoteKeysDao().remoteKeysMovieId(repoId)
            }
        }
    }
}