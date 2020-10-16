package ph.movieguide.com.di

import androidx.paging.ExperimentalPagingApi
import org.koin.dsl.module
import ph.movieguide.com.features.cinema.paging.PagingMediator
import ph.movieguide.com.features.search.result.repository.ResultMediator

@ExperimentalPagingApi
val mediatorModule = module {
  factory { PagingMediator(apiServices = get(), appDatabase = get(), mapper = get()) }
  factory { ResultMediator(query = get(), apiService = get(), database = get()) }
}