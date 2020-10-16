package ph.movieguide.com.features.visited

import kotlinx.coroutines.flow.Flow
import ph.movieguide.android_baseplate_persistence.AppDatabase
import ph.movieguide.android_baseplate_persistence.model.DBVisitedMovie
import ph.movieguide.com.data.mapper.VisitedMapper

class Repository(private val appDatabase: AppDatabase,
private val mapper: VisitedMapper) : DataSource {
    override suspend fun getVisitedDBList(): Flow<List<DBVisitedMovie>> {
        return appDatabase.visitedDao().getVisitedMovies()
    }

}