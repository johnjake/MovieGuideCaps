package ph.movieguide.android_baseplate_persistence.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ph.movieguide.android_baseplate_persistence.model.DBMoviesNowPlaying

@Dao
abstract class NowPlayingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertMovieScreen(topRated: DBMoviesNowPlaying)

    @Query("select * from movie_playing where idMovie = :movieId")
    abstract suspend fun getNowPlayingMovieById(movieId: Int): DBMoviesNowPlaying

    @Query("select * from movie_playing")
    abstract fun getNowPlayingMovies(): Flow<List<DBMoviesNowPlaying>>

    @Query("SELECT * FROM movie_playing WHERE id BETWEEN :idStart AND :idEnd")
    abstract fun getNowPlayingSuspendBetweenId(idStart: Int, idEnd: Int): List<DBMoviesNowPlaying>

    @Query("SELECT * FROM movie_playing WHERE id BETWEEN :idStart AND :idEnd")
    abstract fun getNowPlayingBetweenId(idStart: Int, idEnd: Int): List<DBMoviesNowPlaying>

    @Query("SELECT * FROM movie_playing")
    abstract fun getNowShowingMoviePaging(): PagingSource<Int, DBMoviesNowPlaying>

    @Query("DELETE FROM movie_playing")
    abstract suspend fun clearAllNowPlayingMovie()
}