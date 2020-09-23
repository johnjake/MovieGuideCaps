package ph.movieguide.com.interactors

import ph.movieguide.com.data.vo.MovieScreen

interface SplashScreenInteractor {
    suspend fun getTopRatedMovies(
        languages: String,
        pageNumber: Int
    ): List<MovieScreen>
}