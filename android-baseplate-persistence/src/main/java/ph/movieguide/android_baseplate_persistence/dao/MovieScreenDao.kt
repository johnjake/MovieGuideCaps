package ph.movieguide.android_baseplate_persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ph.movieguide.android_baseplate_persistence.model.DBMovieScreen

@Dao
abstract class MovieScreenDao: BaseDao<DBMovieScreen> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertMovieScreen(movieScreen: DBMovieScreen)

    @Query("select * from movie_screen where idMovie = :movieId")
    abstract fun getMovieScreenById(movieId: Int): DBMovieScreen

    @Query("select * from movie_screen")
    abstract fun getMovieScreen(): Flow<List<DBMovieScreen>>
}