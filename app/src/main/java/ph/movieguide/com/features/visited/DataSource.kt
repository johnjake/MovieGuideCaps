package ph.movieguide.com.features.visited

import kotlinx.coroutines.flow.Flow
import ph.movieguide.android_baseplate_persistence.model.DBVisitedMovie

interface DataSource {
    suspend fun getVisitedDBList(): Flow<List<DBVisitedMovie>>
}