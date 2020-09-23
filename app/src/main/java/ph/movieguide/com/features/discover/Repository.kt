package ph.movieguide.com.features.discover

import kotlinx.coroutines.flow.Flow
import org.koin.dsl.module
import ph.movieguide.android_baseplate_persistence.AppDatabase
import ph.movieguide.android_baseplate_persistence.model.DBMovieDiscover

val discoverRepoModule = module {
    factory { Repository(appDatabase = get()) }
}
class Repository(private val appDatabase: AppDatabase) : DataSource {
    override fun getDiscoverMovies(): Flow<List<DBMovieDiscover>> {
        return appDatabase.discoverDao().getDiscoverMovies()
    }

    override fun getFilterMovies(): Flow<List<DBMovieDiscover>> {
        return appDatabase.discoverDao().getFilterMovies()
    }
}