package ph.movieguide.com.features.search

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
import ph.movieguide.com.features.search.result.ResultMediator
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import java.io.InvalidObjectException
import java.lang.Exception

private const val STARTING_PAGE_INDEX = 1

@OptIn(ExperimentalPagingApi::class)
class SearchPagingMediator(
    private val query: String,
    private val apiServices: ApiServices,
    private val appDatabase: AppDatabase
) : RemoteMediator<Int, DBMovieDiscover>() {

    private val mapper by lazy { GlobalContext.get().koin.get<DiscoverMapper>() }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, DBMovieDiscover>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: STARTING_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                try {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    // If the previous key is null, then we can't request more data
                    remoteKeys?.prevKey ?: return MediatorResult.Success(endOfPaginationReached = true)
                    remoteKeys.prevKey
                } catch (ex: Exception) {
                    Timber.e("Remote key and the prevKey should not be null")
                }

            }
            LoadType.APPEND -> {
                try {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    remoteKeys?.nextKey
                } catch (ex: Exception) {
                    Timber.e("Remote key should not be null for $loadType")
                }
            }
        }

        try {
            val apiKey: String = BuildConfig.API_KEY
            val response = page?.let { apiServices.getSearchMovieView(apiKey, query, it as Int) }
            val isEndOfList = response?.results?.isEmpty()
            appDatabase.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    appDatabase.remoteKeysDao().clearRemoteKeys()
                    appDatabase.nowPlayingMovieDao().clearAllNowPlayingMovie()
                }
                val prevKey = if (page == ResultMediator.DEFAULT_PAGE_INDEX) null else {page as Int - 1}
                val nextKey = if (isEndOfList != false) null else { page as Int + 1 }
                val keys = response?.results?.map {
                    DBRemoteKeys(repoId = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                keys.let { it?.let { it1 -> appDatabase.remoteKeysDao().insertAll(it1) } }
                val apiList = response?.results
                apiList?.forEach {
                    val dbMovie = mapper.mapFromData(it)
                    appDatabase.discoverDao().insertMovieScreen(dbMovie)
                }
            }
            return MediatorResult.Success(endOfPaginationReached = !response?.results?.isEmpty()!!)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, DBMovieDiscover>): DBRemoteKeys? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { movie ->
                // Get the remote keys of the last item retrieved
                appDatabase.remoteKeysDao().remoteKeysMovieId(movie.id)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, DBMovieDiscover>): DBRemoteKeys? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { repo ->
                // Get the remote keys of the first items retrieved
                appDatabase.remoteKeysDao().remoteKeysMovieId(repo.id)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, DBMovieDiscover>
    ): DBRemoteKeys? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { repoId ->
                appDatabase.remoteKeysDao().remoteKeysMovieId(repoId)
            }
        }
    }
}