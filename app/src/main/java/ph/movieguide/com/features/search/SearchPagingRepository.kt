package ph.movieguide.com.features.search

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ph.movieguide.com.api.ApiServices
import ph.movieguide.com.data.vo.MovieScreen
import ph.movieguide.com.features.visited.SearchPagingSource

class SearchPagingRepository(private val api: ApiServices) : SearchDataSource {
    override fun getSearchResultStream(queries: String): Flow<PagingData<MovieScreen>> {
        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = { SearchPagingSource(query = queries, apiServices = api) }
            ).flow
    }
    companion object {
        private const val NETWORK_PAGE_SIZE = 50
    }
}