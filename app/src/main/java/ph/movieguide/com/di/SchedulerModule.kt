package ph.movieguide.com.di

import org.koin.dsl.module
import ph.movieguide.com.utils.schedulers.BaseSchedulerProvider
import ph.movieguide.com.utils.schedulers.SchedulerProvider

val schedulerModule = module {
    single { providesSchedulerSource() }
}

fun providesSchedulerSource(): BaseSchedulerProvider =
    SchedulerProvider.getInstance()
