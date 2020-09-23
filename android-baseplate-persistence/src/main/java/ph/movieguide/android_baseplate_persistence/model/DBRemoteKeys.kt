package ph.movieguide.android_baseplate_persistence.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = DBRemoteKeys.TABLE_NAME)
data class DBRemoteKeys(
    @PrimaryKey(autoGenerate = true)
    var repoId: Int? = 0,
    val prevKey: Int? = 0,
    val nextKey: Int? = 0) {
    companion object {
        const val TABLE_NAME = "movie_remote_keys"
    }
}