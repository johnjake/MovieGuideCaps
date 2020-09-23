package ph.movieguide.android_baseplate_persistence.dao

import androidx.room.Dao
import androidx.room.Query
import io.reactivex.Single
import ph.movieguide.android_baseplate_persistence.model.DBToken

@Dao
interface TokenDao{

    @Query("SELECT * FROM ${DBToken.TOKEN_TABLE_NAME} LIMIT 1")
    fun getToken(): Single<DBToken>

    @Query("SELECT * FROM ${DBToken.TOKEN_TABLE_NAME} LIMIT 1")
    fun getBearerToken(): DBToken

    @Query("DELETE FROM ${DBToken.TOKEN_TABLE_NAME}")
    fun logoutToken()
}