package ph.movieguide.com.features.search.result

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import ph.movieguide.android_baseplate_persistence.model.DBMoviesNowPlaying
import ph.movieguide.com.features.search.result.repository.ResultRepository

class ResultViewModel(private val integrator: ResultRepository) : ViewModel() {
    @ExperimentalPagingApi
    fun getResultFromSearch(query: String): Flow<PagingData<DBMoviesNowPlaying>> {
        return integrator
            .setMovieDBNowPlaying(query)
            .cachedIn(viewModelScope)
    }
}