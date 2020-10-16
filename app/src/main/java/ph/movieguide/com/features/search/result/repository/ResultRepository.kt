package ph.movieguide.com.features.search.result.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ph.movieguide.android_baseplate_persistence.AppDatabase
import ph.movieguide.android_baseplate_persistence.model.DBMoviesNowPlaying
import ph.movieguide.com.api.ApiServices

class ResultRepository(
    private val api: ApiServices,
    private val appDatabase: AppDatabase
)  {

    companion object {
        const val DEFAULT_PAGE_INDEX = 1
        private const val NETWORK_PAGE_SIZE = 50
    }

    @ExperimentalPagingApi
    fun setMovieDBNowPlaying(query: String): Flow<PagingData<DBMoviesNowPlaying>> {
        val dbQuery = "%${query.replace(' ', '%')}%"
        val pagingSourceFactory = { appDatabase.nowPlayingMovieDao().getSearchMoviePlaying(dbQuery) }
        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = ResultMediator(
                query, api, appDatabase
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }
}