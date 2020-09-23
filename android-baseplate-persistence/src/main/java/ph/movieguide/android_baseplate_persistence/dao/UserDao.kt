package ph.movieguide.android_baseplate_persistence.dao

import androidx.room.Dao
import androidx.room.Query
import io.reactivex.Single
import ph.movieguide.android_baseplate_persistence.model.DBUserSession

@Dao
interface UserDao {

    @Query("SELECT * FROM ${DBUserSession.USER_TABLE_NAME} LIMIT 1")
    fun getUserInfo(): Single<DBUserSession>

    @Query("SELECT uid FROM ${DBUserSession.USER_TABLE_NAME} LIMIT 1")
    fun getUserId(): Single<String>

    @Query("DELETE FROM ${DBUserSession.USER_TABLE_NAME}")
    fun logoutUser()

    @Query("UPDATE ${DBUserSession.USER_TABLE_NAME} SET full_name = :fullName, profile_photo_url = :photoUrl WHERE email = :email")
    fun updateUser(fullName: String, email: String, photoUrl: String): Int

    @Query("SELECT email FROM ${DBUserSession.USER_TABLE_NAME} LIMIT 1")
    fun getEmail(): Single<String>

    // abstract fun update(user: UserSession)
}