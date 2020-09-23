package ph.movieguide.com.interactors

import kotlinx.coroutines.flow.Flow
import ph.movieguide.android_baseplate_persistence.model.DBMoviesTopRated

interface CinemaInteractor {
    fun getTopRatedMoviesFromDB(): Flow<List<DBMoviesTopRated>>
}