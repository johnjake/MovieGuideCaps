package ph.movieguide.android_baseplate_persistence.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ph.movieguide.android_baseplate_persistence.model.DBMovieDiscover

@Dao
abstract class DiscoverDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertMovieScreen(topRated: DBMovieDiscover)

    @Query("select * from movie_discover where idMovie = :movieId")
    abstract suspend fun getDiscoverMovieById(movieId: Int): DBMovieDiscover

    @Query("select * from movie_discover")
    abstract fun getDiscoverMovies(): Flow<List<DBMovieDiscover>>

    @Query("select * from movie_discover group by title")
    abstract fun getFilterMovies(): Flow<List<DBMovieDiscover>>

    @Query("SELECT * FROM movie_discover WHERE id BETWEEN :idStart AND :idEnd")
    abstract fun getNowPlayingSuspendBetweenId(idStart: Int, idEnd: Int): List<DBMovieDiscover>

    @Query("SELECT * FROM movie_discover WHERE id BETWEEN :idStart AND :idEnd")
    abstract fun getDiscoverBetweenId(idStart: Int, idEnd: Int): List<DBMovieDiscover>

    @Query("SELECT * FROM movie_discover")
    abstract fun getDiscoverMoviePaging(): PagingSource<Int, DBMovieDiscover>

    @Query("DELETE FROM movie_discover")
    abstract suspend fun clearAllDiscoverMovie()
}