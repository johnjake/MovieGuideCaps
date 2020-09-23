package ph.movieguide.com.features.cinema

import kotlinx.coroutines.flow.Flow
import ph.movieguide.android_baseplate_persistence.model.DBMoviesTopRated
import ph.movieguide.com.interactors.CinemaInteractor
import javax.inject.Inject

class CinemaImpl (
    private val repository: Repository
) : CinemaInteractor {
    override fun getTopRatedMoviesFromDB(): Flow<List<DBMoviesTopRated>> = repository.getTopRatedMoviesFromDB()
}