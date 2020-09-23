package ph.movieguide.com.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DBTypeConverter() {
    @TypeConverter
    fun <T> saveList(listOfString: List<T?>?): String? {
        return Gson().toJson(listOfString)
    }

    @TypeConverter
    fun <T : Comparable<T>> restoreList(paramList: String?): ArrayList<T> {
        return Gson().fromJson(paramList, object : TypeToken<List<T?>?>() {}.type)
    }

    companion object {

        private var INSTANCE: DBTypeConverter? = null

        fun getInstance(): DBTypeConverter =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: DBTypeConverter().also { INSTANCE = it }
            }
    }
}