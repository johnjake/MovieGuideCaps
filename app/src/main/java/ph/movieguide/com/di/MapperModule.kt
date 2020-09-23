package ph.movieguide.com.di

import org.koin.dsl.module
import ph.movieguide.com.data.mapper.DiscoverMapper
import ph.movieguide.com.data.mapper.MovieMapper
import ph.movieguide.com.data.mapper.NowPlayingMapper
import ph.movieguide.com.data.mapper.TopRatedMapper
import ph.movieguide.com.data.mapper.VisitedMapper

val mapperModule = module {
    single { providesMovieScreeningMapper() }
    single { providesTopRatedMovieMapper() }
    single { providesNowPlayingMovieMapper() }
    single { providesVisitedMovieMapper() }
    single { providesDiscoverMovieMapper() }
}
fun providesMovieScreeningMapper(): MovieMapper {
    return MovieMapper.getInstance()
}

fun providesTopRatedMovieMapper(): TopRatedMapper {
    return TopRatedMapper.getInstance()
}

fun providesNowPlayingMovieMapper(): NowPlayingMapper {
    return NowPlayingMapper.getInstance()
}

fun providesVisitedMovieMapper(): VisitedMapper {
    return VisitedMapper.getInstance()
}

fun providesDiscoverMovieMapper(): DiscoverMapper {
    return DiscoverMapper.getInstance()
}
