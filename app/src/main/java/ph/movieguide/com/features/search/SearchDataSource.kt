package ph.movieguide.com.features.search

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ph.movieguide.com.data.vo.MovieScreen
import ph.movieguide.com.features.cinema.Repository

interface SearchDataSource {
    fun getSearchResultStream(query: String, pagingConfig: PagingConfig) : Flow<PagingData<MovieScreen>>
    fun getDefaultPageConfig(): PagingConfig
}