package ph.movieguide.com.features.cinema

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.DataSource
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.dsl.viewModel

import org.koin.dsl.module
import ph.movieguide.android_baseplate_persistence.AppDatabase
import ph.movieguide.android_baseplate_persistence.model.DBMoviesNowPlaying
import ph.movieguide.com.data.mapper.TopRatedMapper
import ph.movieguide.com.data.vo.MovieScreen
import ph.movieguide.com.features.cinema.paging.PageDataSource
import ph.movieguide.com.features.splash.ViewModelSplash
import ph.movieguide.com.utils.State
import timber.log.Timber

val viewModelCinema =  module {
    viewModel { ViewModelCinema(integrator = get(), appDatabase = get(), mapper = get()) }
}
class ViewModelCinema (
    private val integrator: Repository,
    private val appDatabase: AppDatabase,
    private val mapper: TopRatedMapper
) : ViewModel() {

    companion object {
        private const val VISIBLE_THRESHOLD = 5
    }

    private val jobCoroutine = Job()
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Timber.e("Error Occurred at: $throwable")
    }

    private val coroutineScope = CoroutineScope(Dispatchers.IO + jobCoroutine + exceptionHandler)

    private var movieScreenDB: MutableList<MovieScreen> = arrayListOf()

    /** coroutine movie screen from DB **/
    private val topMutableMovie = MutableLiveData<State<List<MovieScreen>>>()
    val stateTopMovieDB: LiveData<State<List<MovieScreen>>>
        get() = topMutableMovie

    /** coroutine  now playing from API **/
    private val nowPlayingMutableLive = MutableLiveData<State<List<MovieScreen>>>()
    val stateNowPlaying: LiveData<State<List<MovieScreen>>>
        get() = nowPlayingMutableLive

    fun getTopRatedMoviesFromDB() {
        coroutineScope.launch {
            val topMovieList = integrator.getTopRatedMoviesFromDB()
            topMovieList.collect { list ->
                list.forEach {
                    val movie = mapper.mapFromStorage(it)
                    movieScreenDB.add(movie)
                }
                val dataPack = State.Data(movieScreenDB)
                topMutableMovie.postValue(dataPack)
            }
        }
    }

    fun getNowPlayingMoviesFromApi(pageNumber: Int) {
        coroutineScope.launch {
            viewModelScope.launch(Dispatchers.IO) {
                val movieResponses = integrator.getMoviesNowPlaying(pageNumber)
                val dataPack = State.Data(movieResponses)
                nowPlayingMutableLive.postValue(dataPack)
            }
        }
    }

    fun listScrolled(visibleItemCount: Int, lastVisibleItemPosition: Int, totalItemCount: Int) {
        if (visibleItemCount + lastVisibleItemPosition + VISIBLE_THRESHOLD >= totalItemCount) {
            viewModelScope.launch {
            }
        }
    }

    @ExperimentalPagingApi
    fun fetchMovieNowShowing(): Flow<PagingData<DBMoviesNowPlaying>> {
        return integrator.letMovieScreenFlowDb().cachedIn(viewModelScope)
    }

    override fun onCleared() {
        super.onCleared()
        jobCoroutine.cancel()
    }
}