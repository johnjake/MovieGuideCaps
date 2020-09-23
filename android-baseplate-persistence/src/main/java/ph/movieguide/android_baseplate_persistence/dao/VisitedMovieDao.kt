package ph.movieguide.android_baseplate_persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

import ph.movieguide.android_baseplate_persistence.model.DBVisitedMovie

@Dao
abstract class VisitedMovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertMovieScreen(movieScreen: DBVisitedMovie)

    @Query("select * from movie_visited where idMovie = :movieId")
    abstract suspend fun getVisitedMovieById(movieId: Int): DBVisitedMovie

    @Query("select * from movie_visited group by title")
    abstract fun getVisitedMovies(): Flow<List<DBVisitedMovie>>
}