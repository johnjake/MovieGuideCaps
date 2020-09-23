package ph.movieguide.com.features.splash

import ph.movieguide.com.data.vo.MovieScreen
import ph.movieguide.com.interactors.SplashScreenInteractor
import javax.inject.Inject

class SplashScreenImpl @Inject constructor(
    private val repository: Repository
) : SplashScreenInteractor {

    override suspend fun getTopRatedMovies(languages: String, pageNumber: Int): List<MovieScreen> = repository.getTopRatedMovies(languages, pageNumber)



}