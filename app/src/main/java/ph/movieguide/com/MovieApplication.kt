package ph.movieguide.com

import android.app.Application
import androidx.paging.ExperimentalPagingApi
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import ph.movieguide.android_baseplate_persistence.utils.databaseModule
import ph.movieguide.com.features.splash.splashRepoModule
import ph.movieguide.com.features.splash.viewModelSplash
import ph.movieguide.com.di.mapperModule
import ph.movieguide.com.di.networkModule
import ph.movieguide.com.di.schedulerModule
import ph.movieguide.com.di.storageModule
import ph.movieguide.com.features.cinema.cinemaRepoModule
import ph.movieguide.com.features.cinema.paging.mediatorModule
import ph.movieguide.com.features.cinema.viewModelCinema
import ph.movieguide.com.features.details.detailsRepoModuleInfo
import ph.movieguide.com.features.details.viewModelDataDetails
import ph.movieguide.com.features.discover.discoverRepoModule
import ph.movieguide.com.features.discover.searchDiscover
import ph.movieguide.com.features.main.mainDataSourceModule
import ph.movieguide.com.features.main.mainRepoModule
import ph.movieguide.com.features.main.viewModelMain
import ph.movieguide.com.features.search.searchRepoModule
import ph.movieguide.com.features.search.viewModelSearch
import ph.movieguide.com.features.visited.viewModelVisited
import ph.movieguide.com.features.visited.visitedRepoModule

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
                viewModelSplash,
                viewModelMain,
                mainRepoModule,
                mainDataSourceModule,
                viewModelCinema,
                splashRepoModule,
                mediatorModule,
                cinemaRepoModule,
                detailsRepoModuleInfo,
                viewModelDataDetails,
                searchRepoModule,
                viewModelSearch,
                visitedRepoModule,
                discoverRepoModule,
                searchDiscover,
                viewModelVisited
            ))
        }
    }
}