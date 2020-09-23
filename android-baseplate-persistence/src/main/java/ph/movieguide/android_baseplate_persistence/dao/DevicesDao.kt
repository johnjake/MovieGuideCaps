package ph.movieguide.android_baseplate_persistence.dao

import androidx.room.Dao
import androidx.room.Query
import io.reactivex.Maybe
import ph.movieguide.android_baseplate_persistence.model.Devices

@Dao
interface DevicesDao {

    @Query("SELECT * FROM ${Devices.TABLE} LIMIT 1")
    fun getDeviceId(): Maybe<String>
}