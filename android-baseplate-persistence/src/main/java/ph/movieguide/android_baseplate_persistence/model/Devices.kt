package ph.movieguide.android_baseplate_persistence.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = Devices.TABLE)
data class Devices(
    @PrimaryKey
    @ColumnInfo(name = "deviceId")
    val deviceId: String = ""
) {
    companion object {
        const val TABLE = "devicesTbl"
    }
}