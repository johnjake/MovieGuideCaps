package ph.movieguide.com.features.details

import ph.movieguide.com.data.vo.MovieScreen

interface DataSource {
    suspend fun getMovieDetailsById(movieId: Int): MovieScreen
    suspend fun getTopRatedMovieById(movieId: Int): MovieScreen
    suspend fun getDiscoverMovieById(movieId: Int): MovieScreen
    suspend fun insertMovie(movie: MovieScreen)
}