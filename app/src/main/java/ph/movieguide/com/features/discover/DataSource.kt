package ph.movieguide.com.features.discover

import kotlinx.coroutines.flow.Flow
import ph.movieguide.android_baseplate_persistence.model.DBMovieDiscover

interface DataSource {
    fun getDiscoverMovies(): Flow<List<DBMovieDiscover>>
    fun getFilterMovies(): Flow<List<DBMovieDiscover>>
}