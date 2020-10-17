package ph.movieguide.com.features.search

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ph.movieguide.com.data.vo.MovieScreen

interface SearchDataSource {
    fun getSearchResultStream(query: String) : Flow<PagingData<MovieScreen>>
}