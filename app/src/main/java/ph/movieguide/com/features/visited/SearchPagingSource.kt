package ph.movieguide.com.features.visited

import androidx.paging.PagingSource
import ph.movieguide.com.BuildConfig
import ph.movieguide.com.api.ApiServices
import ph.movieguide.com.data.vo.MovieScreen
import retrofit2.HttpException

private const val STARTING_PAGE_INDEX = 1

class SearchPagingSource(
    private val query: String,
    private val apiServices: ApiServices) : PagingSource<Int, MovieScreen>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieScreen> {
        val position = params.key ?: STARTING_PAGE_INDEX
        val apiKey = BuildConfig.API_KEY
        return try {
            val response = apiServices.getSearchMovieView(apiKey, query, position)
            val dataResult = response.results

            LoadResult.Page(
                data = dataResult,
                prevKey = if (position == STARTING_PAGE_INDEX) null else position -1,
                nextKey = if (dataResult.isEmpty()) null else position + 1
            )

        } catch (ex: Exception) {
            LoadResult.Error(ex)
        } catch (ex: HttpException) {
            LoadResult.Error(ex)
        }

    }
}