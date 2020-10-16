package ph.movieguide.com.features.cinema.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingSource
import ph.movieguide.com.BuildConfig
import ph.movieguide.com.api.ApiServices
import ph.movieguide.com.data.vo.MovieScreen
import ph.movieguide.com.features.cinema.Repository.Companion.DEFAULT_LANGUAGE
import ph.movieguide.com.features.cinema.Repository.Companion.DEFAULT_PAGE_INDEX
import retrofit2.HttpException
import java.io.IOException

@ExperimentalPagingApi
class PageDataSource(private val api: ApiServices) :
    PagingSource<Int, MovieScreen>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieScreen> {
        val page = params.key ?: DEFAULT_PAGE_INDEX
        val apiKey = BuildConfig.API_KEY
        return try {
            val response = api.getNowPlayingMovies(apiKey, DEFAULT_LANGUAGE, page)
            LoadResult.Page(
                response.results,
                prevKey = if (page == DEFAULT_PAGE_INDEX) null else page - 1,
                nextKey = if (response.results.isEmpty()) null else page + 1
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }
}