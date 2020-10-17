package ph.movieguide.com.features.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import ph.movieguide.com.data.vo.MovieScreen

class SearchPagingViewModel(
    private val integrator: SearchPagingRepository
) : ViewModel() {

    private var currentQuery: String? = null

    private var currentFlowResult: Flow<PagingData<MovieScreen>>? = null

    fun searchMovies(query: String): Flow<PagingData<MovieScreen>> {
        val lastFlowResult = currentFlowResult
        if (query == currentQuery && lastFlowResult != null) {
            return lastFlowResult
        }
        currentQuery = query

        val newFlowResult: Flow<PagingData<MovieScreen>> = integrator.getSearchResultStream(query).cachedIn(viewModelScope)
        currentFlowResult = newFlowResult
        return newFlowResult
    }
}