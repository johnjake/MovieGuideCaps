package ph.movieguide.com.features.search

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import ph.movieguide.android_baseplate_persistence.AppDatabase
import ph.movieguide.android_baseplate_persistence.model.DBMovieDiscover
import ph.movieguide.com.api.ApiServices

@OptIn(ExperimentalPagingApi::class)
class SearchPagingMediator(
    private val query: String,
    private val apiServices: ApiServices,
    private val appDatabase: AppDatabase
) : RemoteMediator<Int, DBMovieDiscover>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, DBMovieDiscover>
    ): MediatorResult {
        TODO("Not yet implemented")
    }
    
}