package ph.movieguide.com.di

import android.app.Application
import android.content.Context
import org.koin.dsl.module

val appModule = module {
    single { providesApplicationContext(get()) }
}
fun providesApplicationContext(app: Application): Context {
    return app.applicationContext
}
