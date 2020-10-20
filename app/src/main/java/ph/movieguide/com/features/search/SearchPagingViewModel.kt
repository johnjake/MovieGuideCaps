package ph.movieguide.com.features.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import ph.movieguide.android_baseplate_persistence.model.DBMovieDiscover

class SearchPagingViewModel(
    private val integrator: SearchPagingRepository
) : ViewModel() {

    private var currentQuery: String? = null

    private var currentFlowResult: Flow<PagingData<DBMovieDiscover>>? = null

    @ExperimentalPagingApi
    fun searchMovies(query: String): Flow<PagingData<DBMovieDiscover>> {
        val lastFlowResult = currentFlowResult
        if (query == currentQuery && lastFlowResult != null) return lastFlowResult
        currentQuery = query
        val newFlowResult: Flow<PagingData<DBMovieDiscover>> = integrator
            .getSearchResultStream(query)
            .cachedIn(viewModelScope)

        currentFlowResult = newFlowResult
        return newFlowResult
    }
}