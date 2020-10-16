package ph.movieguide.com.features.search.result.repository

import androidx.paging.LoadType
import androidx.paging.PagingState
import ph.movieguide.android_baseplate_persistence.model.DBMoviesNowPlaying
import ph.movieguide.android_baseplate_persistence.model.DBRemoteKeys

interface MediatorSource {
    suspend fun getKeyPageData(loadType: LoadType, state: PagingState<Int, DBMoviesNowPlaying>): Any?
    suspend fun getLastRemoteKey(state: PagingState<Int, DBMoviesNowPlaying>): DBRemoteKeys?
    suspend fun getFirstRemoteKey(state: PagingState<Int, DBMoviesNowPlaying>): DBRemoteKeys?
    suspend fun getClosestRemoteKey(state: PagingState<Int, DBMoviesNowPlaying>): DBRemoteKeys?
}