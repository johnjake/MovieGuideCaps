package ph.movieguide.com.features.search

import ph.movieguide.com.data.vo.MovieMetaData

sealed class PagingSearchState {
    data class Success(val data: MovieMetaData) : PagingSearchState()
    data class Error(val error: Throwable) : PagingSearchState()
}