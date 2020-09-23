package ph.movieguide.com.api

import io.reactivex.Single
import ph.movieguide.com.data.vo.MovieDetails
import ph.movieguide.com.data.vo.MovieMetaData
import ph.movieguide.com.features.main.DiscoverResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiServices {

    @GET("movie/550")
    fun movieDetails(
        @Query("api_key") apiKey: String
    ): Single<MovieDetails>

    @GET("movie/{movieCode}")
    suspend fun getMovieDetails(
        @Path("movieCode") movieCode: Int,
        @Query("api_key") apiKey: String
    ): MovieDetails

    /** Movie Discover **/

    @GET("discover/movie")
    suspend fun getDiscoverMovies(
        @Query("api_key") apiKey: String,
        @Query("page") pageNumber: Int
    ): MovieMetaData

    @GET("search/movie")
    suspend fun getSearchMovies(
        @Query("api_key") apiKey: String,
        @Query("query") searchKey: String,
        @Query("page") pageNumber: Int
    ): MovieMetaData

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("api_key") apiKey: String,
        @Query("language") languages: String = "en-US",
        @Query("page") pageNumber: Int
    ): MovieMetaData

    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("api_key") apiKey: String,
        @Query("language") languages: String = "en-US",
        @Query("page") pageNumber: Int
    ): MovieMetaData
}