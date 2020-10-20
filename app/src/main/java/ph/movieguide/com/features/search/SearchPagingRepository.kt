package ph.movieguide.com.features.search

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ph.movieguide.android_baseplate_persistence.AppDatabase
import ph.movieguide.android_baseplate_persistence.model.DBMovieDiscover
import ph.movieguide.com.api.ApiServices
import ph.movieguide.com.data.mapper.DiscoverMapper
import ph.movieguide.com.features.cinema.Repository

class SearchPagingRepository(
    private val apiServices: ApiServices,
    private val appDatabase: AppDatabase,
    private val mapper: DiscoverMapper) {

    @ExperimentalPagingApi
    fun getSearchResultStream(query: String, pagingConfig: PagingConfig = getDefaultPageConfig()): Flow<PagingData<DBMovieDiscover>> {
        val pagingSourceFactory = { appDatabase.discoverDao().getSearchMoviePlaying(query) }
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = pagingSourceFactory,
            remoteMediator = SearchPagingMediator(query, apiServices, appDatabase, mapper)
        ).flow
    }

    private fun getDefaultPageConfig(): PagingConfig {
        return PagingConfig(pageSize = Repository.DEFAULT_PAGE_SIZE, enablePlaceholders = true)
    }
}