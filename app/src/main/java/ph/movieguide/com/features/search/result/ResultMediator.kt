package ph.movieguide.com.features.search.result

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import org.koin.core.context.GlobalContext
import ph.movieguide.android_baseplate_persistence.AppDatabase
import ph.movieguide.android_baseplate_persistence.model.DBMovieDiscover
import ph.movieguide.android_baseplate_persistence.model.DBRemoteKeys
import ph.movieguide.com.BuildConfig
import ph.movieguide.com.api.ApiServices
import ph.movieguide.com.data.mapper.DiscoverMapper
import retrofit2.HttpException
import java.io.IOException

@ExperimentalPagingApi
class ResultMediator(private val query: String,
    private val apiService: ApiServices,
    val database: AppDatabase) :
    RemoteMediator<Int, DBMovieDiscover>() {

    val mapper by lazy { GlobalContext.get().koin.get<DiscoverMapper>() }

    override suspend fun load(
        loadType: LoadType, state: PagingState<Int, DBMovieDiscover>
    ): MediatorResult {

        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getClosestRemoteKey(state)
                remoteKeys?.nextKey?.minus(1) ?: DEFAULT_PAGE_INDEX
            }
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                val remoteKeys = getLastRemoteKey(state)
                if (remoteKeys?.nextKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
                remoteKeys.nextKey
            }
        }

        try {
            val apiKey: String = BuildConfig.API_KEY
            val response = page?.let { apiService.getSearchMovies(apiKey, query, it) }
            val isEndOfList = response?.results?.isEmpty()
            database.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    database.remoteKeysDao().clearRemoteKeys()
                    database.nowPlayingMovieDao().clearAllNowPlayingMovie()
                }
                val prevKey = if (page == DEFAULT_PAGE_INDEX) null else page?.minus(1)
                val nextKey = if (isEndOfList != false) null else page.plus(1)
                val keys = response?.results?.map {
                    DBRemoteKeys(repoId = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                keys.let { it?.let { it1 -> database.remoteKeysDao().insertAll(it1) } }
                val apiList = response?.results
                apiList?.forEach {
                    val dbMovie = mapper.mapFromData(it)
                    database.discoverDao().insertMovieScreen(dbMovie)
                }
            }
            return MediatorResult.Success(endOfPaginationReached = !response?.results?.isEmpty()!!)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    /**
     * this returns the page key or the final end of list success result
     */
    private suspend fun getKeyPageData(loadType: LoadType, state: PagingState<Int, DBMovieDiscover>): Any? {
        return when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getClosestRemoteKey(state)
                remoteKeys?.nextKey?.minus(1) ?: DEFAULT_PAGE_INDEX
            }
            LoadType.APPEND -> {
                val remoteKeys = getLastRemoteKey(state)
                if (remoteKeys?.nextKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
                remoteKeys.nextKey
            }
            LoadType.PREPEND -> {
                return MediatorResult.Success(endOfPaginationReached = true)
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
            ?.let { movie -> database.remoteKeysDao().remoteKeysMovieId(movie.id) }
    }

    /**
     * get the first remote key inserted which had the data
     */
    private suspend fun getFirstRemoteKey(state: PagingState<Int, DBMovieDiscover>): DBRemoteKeys? {
        return state.pages
            .firstOrNull() { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { movie -> database.remoteKeysDao().remoteKeysMovieId(movie.id) }
    }

    /**
     * get the closest remote key inserted which had the data
     */
    private suspend fun getClosestRemoteKey(state: PagingState<Int, DBMovieDiscover>): DBRemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                database.remoteKeysDao().remoteKeysMovieId(id)
            }
        }
    }

    companion object {
        const val DEFAULT_PAGE_INDEX = 1
    }
}