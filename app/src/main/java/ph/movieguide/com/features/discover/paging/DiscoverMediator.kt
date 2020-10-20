package ph.movieguide.com.features.discover.paging

import android.annotation.SuppressLint
import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import ph.movieguide.android_baseplate_persistence.AppDatabase
import ph.movieguide.android_baseplate_persistence.model.DBMovieDiscover
import ph.movieguide.android_baseplate_persistence.model.DBRemoteKeys
import ph.movieguide.com.BuildConfig
import ph.movieguide.com.api.ApiServices
import ph.movieguide.com.data.mapper.DiscoverMapper

import ph.movieguide.com.features.cinema.Repository
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception

@ExperimentalPagingApi
class DiscoverMediator(
    private val apiServices: ApiServices,
    private val appDatabase: AppDatabase,
    private val mapper: DiscoverMapper
) : RemoteMediator<Int, DBMovieDiscover>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, DBMovieDiscover>
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
            val apiKey = BuildConfig.API_KEY
            val language = BuildConfig.DEFAULT_LANGUAGE
            val response = apiServices.getDiscoverMovies(apiKey, page)
            val isEndOfList = response.results.isEmpty()
            appDatabase.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    appDatabase.remoteKeysDao().clearRemoteKeys()
                    appDatabase.discoverDao().clearAllDiscoverMovie()
                }
                val prevKey = if (page == Repository.DEFAULT_PAGE_INDEX) null else page - 1
                val nextKey = if (isEndOfList) null else page + 1
                val dbKeys = response.results.map {
                    DBRemoteKeys(repoId = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                appDatabase.remoteKeysDao().insertAll(dbKeys)
                response.results.forEach { movie ->
                    val domainMovie = mapper.mapFromData(movie)
                    appDatabase.discoverDao().insertMovieScreen(domainMovie)
                }
            }
            return MediatorResult.Success(endOfPaginationReached = isEndOfList)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    @SuppressLint("LogNotTimber")
    private suspend fun getKeyPageData(loadType: LoadType, state: PagingState<Int, DBMovieDiscover>): Any? {
        return when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getClosestRemoteKey(state)
                remoteKeys?.nextKey?.minus(1) ?: Repository.DEFAULT_PAGE_INDEX
            }
            LoadType.APPEND -> {
                try {
                    val remoteKeys = getLastRemoteKey(state)
                    remoteKeys?.nextKey
                }catch (ex: Exception) {
                    Log.e("PREPEND", "message: ${ex.message}")
                }
            }
            LoadType.PREPEND -> {
                try {
                    val remoteKeys = getFirstRemoteKey(state)
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
    private suspend fun getLastRemoteKey(state: PagingState<Int, DBMovieDiscover>): DBRemoteKeys? {
        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { movie -> appDatabase.remoteKeysDao().remoteKeysMovieId(movie.idMovie) }
    }
    /**
     * get the first remote key inserted which had the data
     */
    private suspend fun getFirstRemoteKey(state: PagingState<Int, DBMovieDiscover>): DBRemoteKeys? {
        return state.pages
            .firstOrNull() { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { movie -> appDatabase.remoteKeysDao().remoteKeysMovieId(movie.idMovie) }
    }
    /**
     * get the closest remote key inserted which had the data
     */
    private suspend fun getClosestRemoteKey(state: PagingState<Int, DBMovieDiscover>): DBRemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { repoId ->
                appDatabase.remoteKeysDao().remoteKeysMovieId(repoId)
            }
        }
    }
}