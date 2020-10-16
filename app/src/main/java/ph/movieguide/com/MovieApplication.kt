package ph.movieguide.com

import android.app.Application
import androidx.paging.ExperimentalPagingApi
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import ph.movieguide.android_baseplate_persistence.utils.databaseModule
import ph.movieguide.com.di.implementationModule
import ph.movieguide.com.di.mapperModule
import ph.movieguide.com.di.mediatorModule
import ph.movieguide.com.di.networkModule
import ph.movieguide.com.di.repositoryModule
import ph.movieguide.com.di.schedulerModule
import ph.movieguide.com.di.storageModule
import ph.movieguide.com.di.viewModelModule

class MovieApplication : Application() {

    @ExperimentalPagingApi
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@MovieApplication)
            modules(listOf(
                networkModule,
                storageModule,
                mapperModule,
                schedulerModule,
                databaseModule,
                viewModelModule,
                implementationModule,
                repositoryModule,
                mediatorModule
            ))
        }
    }
}