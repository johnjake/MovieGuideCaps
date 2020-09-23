package ph.movieguide.android_baseplate_persistence.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = DBUserSession.USER_TABLE_NAME)
data class DBUserSession(
    @PrimaryKey
    val uid: String,
    val last_name: String? = "",
    val first_name: String? = "",
    val full_name: String? = "",
    val email: String = "",
    val username: String = "",
    val profile_photo_url: String? = "",
    val country: String? = "",
    val mobile_number: String? = "",
    val address: String? = "",
    val biography: String? = "",
    val last_active_at: String? = "",

) {
    companion object {
        const val USER_TABLE_NAME = "user_session"
    }

    constructor() : this("",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        ""
    )

}