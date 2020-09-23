package ph.movieguide.android_baseplate_persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ph.movieguide.android_baseplate_persistence.model.DBRemoteKeys

@Dao
interface RemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<DBRemoteKeys>)

    @Query("SELECT * FROM movie_remote_keys WHERE repoId = :id")
    suspend fun remoteKeysMovieId(id: Int): DBRemoteKeys?

    @Query("DELETE FROM movie_remote_keys")
    suspend fun clearRemoteKeys()
}