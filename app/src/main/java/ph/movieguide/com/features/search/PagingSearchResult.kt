package ph.movieguide.com.features.search

import ph.movieguide.android_baseplate_persistence.model.DBMovieDiscover

sealed class PagingSearchResult {
    data class Success(val data: List<DBMovieDiscover>) : PagingSearchResult()
    data class Failed(val errors: Throwable) : PagingSearchResult()
}