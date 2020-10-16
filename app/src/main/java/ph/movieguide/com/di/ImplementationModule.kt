package ph.movieguide.com.di

import org.koin.dsl.module
import ph.movieguide.com.features.main.MovieDetailsImpl

val implementationModule = module {
    factory { MovieDetailsImpl(repository = get()) }
}