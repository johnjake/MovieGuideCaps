package ph.movieguide.android_baseplate_persistence.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = DBToken.TOKEN_TABLE_NAME)
data class DBToken(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    var token: String? = "",
    var refresh: String? = ""
) {
    companion object {
        const val TOKEN_TABLE_NAME = "token"
    }

    constructor() : this(0, "", "")

    val bearerToken: String get()= "Bearer $token"
}