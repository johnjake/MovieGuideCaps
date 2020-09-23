package ph.movieguide.android_baseplate_persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ph.movieguide.android_baseplate_persistence.dao.DevicesDao
import ph.movieguide.android_baseplate_persistence.dao.DiscoverDao
import ph.movieguide.android_baseplate_persistence.dao.MovieScreenDao
import ph.movieguide.android_baseplate_persistence.dao.NowPlayingDao
import ph.movieguide.android_baseplate_persistence.dao.RemoteKeysDao
import ph.movieguide.android_baseplate_persistence.dao.TokenDao
import ph.movieguide.android_baseplate_persistence.dao.TopRatedMovieDao
import ph.movieguide.android_baseplate_persistence.dao.UserDao
import ph.movieguide.android_baseplate_persistence.dao.VisitedMovieDao
import ph.movieguide.android_baseplate_persistence.model.DBMovieDiscover
import ph.movieguide.android_baseplate_persistence.model.DBMovieScreen
import ph.movieguide.android_baseplate_persistence.model.DBMoviesNowPlaying
import ph.movieguide.android_baseplate_persistence.model.DBMoviesTopRated
import ph.movieguide.android_baseplate_persistence.model.DBRemoteKeys
import ph.movieguide.android_baseplate_persistence.model.DBToken
import ph.movieguide.android_baseplate_persistence.model.DBUserSession
import ph.movieguide.android_baseplate_persistence.model.DBVisitedMovie
import ph.movieguide.android_baseplate_persistence.model.Devices

@Database(
    entities = [
        DBUserSession::class,
        DBToken::class,
        DBMovieScreen::class,
        DBMoviesTopRated::class,
        DBMoviesNowPlaying::class,
        DBRemoteKeys::class,
        DBVisitedMovie::class,
        DBMovieDiscover::class,
        Devices::class
    ],
    version = 32,
    exportSchema = false
)
@TypeConverters
abstract class AppDatabase : RoomDatabase() {

    abstract fun userSessionDao(): UserDao

    abstract fun tokenDao(): TokenDao

    abstract fun visitedDao(): VisitedMovieDao

    abstract fun discoverDao(): DiscoverDao

    abstract fun devicesDao(): DevicesDao

    abstract fun movieScreenDao(): MovieScreenDao

    abstract fun topRatedMovieDao(): TopRatedMovieDao

    abstract fun nowPlayingMovieDao(): NowPlayingDao

    abstract fun remoteKeysDao(): RemoteKeysDao
}