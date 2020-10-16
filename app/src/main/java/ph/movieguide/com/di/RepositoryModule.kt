package ph.movieguide.com.di

import org.koin.dsl.module
import ph.movieguide.com.features.main.Repository

val repositoryModule = module {
    factory { Repository(api = get(), appDatabase = get(), mapper = get()) }
    factory { ph.movieguide.com.features.splash.Repository(api = get(), appDatabase = get(), mapper = get()) }
    factory { ph.movieguide.com.features.visited.Repository(appDatabase = get(), mapper = get()) }
    factory { ph.movieguide.com.features.details.Repository(appDatabase = get(), mapper = get(), mapperTop = get(), mapperVisited = get()) }
    factory { ph.movieguide.com.features.discover.Repository(appDatabase = get()) }
    factory { ph.movieguide.com.features.search.Repository(apiServices = get(), app = get(), mapper = get()) }
    factory { ph.movieguide.com.features.cinema.Repository(api = get(), appDatabase = get(), mapper = get(), application = get()) }
    factory { ph.movieguide.com.features.search.result.repository.ResultRepository(api = get(), appDatabase = get()) }
}