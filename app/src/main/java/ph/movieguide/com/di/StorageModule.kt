package ph.movieguide.com.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.preference.PreferenceManager
import org.koin.dsl.module
import java.util.*

val storageModule = module {
    single { providesSharedPreferences(get()) }
    single { providesSharedGenres(application = get()) }
}

fun providesSharedPreferences(application: Application): SharedPreferences {
   return PreferenceManager.getDefaultSharedPreferences(application.applicationContext)
}

fun providesSharedGenres(application: Context): Any {
    return try {
        val `is` = application.assets.open("genres.json")
        val size = `is`.available()
        val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
        buffer.toString(Charsets.UTF_8)
    } catch(e: Exception) {
        Log.d("ERROR", "No files converter")
        ""
    }
 }

