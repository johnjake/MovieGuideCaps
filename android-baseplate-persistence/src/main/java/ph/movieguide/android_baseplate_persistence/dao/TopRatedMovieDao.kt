package ph.movieguide.android_baseplate_persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ph.movieguide.android_baseplate_persistence.model.DBMoviesTopRated

@Dao
abstract class TopRatedMovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertMovieScreen(topRated: DBMoviesTopRated)

    @Query("select * from movie_top where idMovie = :movieId")
    abstract suspend fun getTopRatedMovieById(movieId: Int): DBMoviesTopRated

    @Query("select * from movie_top group by original_title")
    abstract fun getTopRatedMovies(): Flow<List<DBMoviesTopRated>>
}