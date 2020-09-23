package ph.movieguide.com.features.splash

import ph.movieguide.com.data.vo.MovieScreen

interface DataSource {
    suspend fun getTopRatedMovies(
        languages: String = "en-US",
        pageNumber: Int
    ): List<MovieScreen>
}