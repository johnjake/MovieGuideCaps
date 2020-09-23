package ph.movieguide.android_baseplate_persistence.utils

import android.app.Application
import androidx.room.Room
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import ph.movieguide.android_baseplate_persistence.AppDatabase
import ph.movieguide.android_baseplate_persistence.dao.DiscoverDao
import ph.movieguide.android_baseplate_persistence.dao.MovieScreenDao
import ph.movieguide.android_baseplate_persistence.dao.NowPlayingDao
import ph.movieguide.android_baseplate_persistence.dao.RemoteKeysDao
import ph.movieguide.android_baseplate_persistence.dao.TopRatedMovieDao
import ph.movieguide.android_baseplate_persistence.dao.VisitedMovieDao

val databaseModule = module {

    fun provideDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(application, AppDatabase::class.java, "movie")
            .fallbackToDestructiveMigration()
            .build()
    }

    fun provideMovieScreenDao(database: AppDatabase): MovieScreenDao {
        return  database.movieScreenDao()
    }

    fun provideTopRatedMovieDao(database: AppDatabase): TopRatedMovieDao {
        return database.topRatedMovieDao()
    }

    fun provideNowPlayingMovieDao(database: AppDatabase): NowPlayingDao {
        return database.nowPlayingMovieDao()
    }

    fun provideRemoteKeysDao(database: AppDatabase): RemoteKeysDao {
        return database.remoteKeysDao()
    }

    fun provideVisitedMovie(database: AppDatabase): VisitedMovieDao {
        return database.visitedDao()
    }

    fun providesDiscoverDao(database: AppDatabase): DiscoverDao {
         return database.discoverDao()
    }

    single { provideDatabase(androidApplication()) }
    single { provideMovieScreenDao(get()) }
    single { provideTopRatedMovieDao(get()) }
    single { provideNowPlayingMovieDao(get()) }
    single { provideRemoteKeysDao(get()) }
    single { provideVisitedMovie(get()) }
}