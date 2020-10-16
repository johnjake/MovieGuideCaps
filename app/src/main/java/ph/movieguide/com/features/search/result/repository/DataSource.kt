package ph.movieguide.com.features.search.result.repository

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ph.movieguide.android_baseplate_persistence.model.DBMoviesNowPlaying

interface DataSource {
    fun setMovieDBNowPlaying(query: String, pagingConfig: PagingConfig): Flow<PagingData<DBMoviesNowPlaying>>
}