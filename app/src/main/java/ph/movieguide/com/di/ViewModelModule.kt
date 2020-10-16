package ph.movieguide.com.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ph.movieguide.com.features.cinema.ViewModelCinema
import ph.movieguide.com.features.details.ViewModelDetails
import ph.movieguide.com.features.discover.ViewModelDiscover
import ph.movieguide.com.features.main.ViewModelMain
import ph.movieguide.com.features.search.ViewModelSearch
import ph.movieguide.com.features.search.result.ResultViewModel
import ph.movieguide.com.features.splash.ViewModelSplash
import ph.movieguide.com.features.visited.ViewModelVisited

val viewModelModule = module {
    viewModel { ViewModelSplash(integrator = get(),schedulers = get(), mapper = get()) }
    viewModel { ViewModelMain(integrator = get(), mapper = get()) }
    viewModel { ViewModelCinema(integrator = get(), appDatabase = get(), mapper = get()) }
    viewModel { ViewModelSearch(integrator = get(), mapper = get()) }
    viewModel { ViewModelVisited(integrator = get(), mapper = get()) }
    viewModel { ViewModelDiscover(integrator = get(), mapper = get()) }
    viewModel { ViewModelDetails(integrator = get()) }
    viewModel { ResultViewModel(integrator = get()) }
}