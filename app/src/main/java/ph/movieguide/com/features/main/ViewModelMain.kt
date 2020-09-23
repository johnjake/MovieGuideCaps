package ph.movieguide.com.features.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ph.movieguide.com.data.mapper.MovieMapper
import ph.movieguide.com.data.vo.MovieDetails
import ph.movieguide.com.data.vo.MovieScreen
import timber.log.Timber
import ph.movieguide.com.utils.State as StateLiveData

val viewModelMain = module {
    viewModel { ViewModelMain(integrator = get(), mapper = get()) }
}
class ViewModelMain(
    private val integrator: MovieDetailsImpl,
    private val mapper: MovieMapper
) : ViewModel() {

    /** for single request scope **/
    private val jobCoroutine = Job()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Timber.e("Error Occurred at: $throwable")
    }

    private val coroutineScope = CoroutineScope(Dispatchers.IO + jobCoroutine + exceptionHandler)

    private var movieScreenDB: MutableList<MovieScreen> = arrayListOf()

    /** rxJava **/
    /**  private val liveData = SingleLiveEvent<RxState>()
    val state: SingleLiveEvent<RxState>
        get() = liveData **/

    /** rxJava **/
    /**  fun getMovieDetailss() {
        integrator.getMovieDetails(550)
            .compose(schedulers.applySchedulers())
            .subscribe(
                { movies ->liveData.value = RxState.MovieDetailsSuccess(movies) },
                { err -> liveData.value = RxState.MovieDetailsError(err) })
            .addTo(disposables)
    } **/

    /** coroutine movie details**/
    private val mutableLiveData = MutableLiveData<StateLiveData<MovieDetails>>()
    val stateMovie: LiveData<StateLiveData<MovieDetails>>
        get() = mutableLiveData

    /** coroutine movie details**/
    @ExperimentalCoroutinesApi
    @Suppress("DeferredResultUnused")
    fun getMovieDetails(movieId: Int) {
        coroutineScope.async(exceptionHandler) {
            viewModelScope.launch(Dispatchers.IO) {
                val movieResponses = integrator.getMovieDetail(movieId)
                val dataPack = StateLiveData.Data(movieResponses)
                mutableLiveData.postValue(dataPack)
            }
        }
    }

    /** coroutine movie discover list**/
    private val discoverMutableMovies = MutableLiveData<StateLiveData<List<ph.movieguide.com.data.vo.MovieScreen>>>()
    val stateDiscover: LiveData<StateLiveData<List<ph.movieguide.com.data.vo.MovieScreen>>>
        get() = discoverMutableMovies

    /** coroutine movie discover **/
    @ExperimentalCoroutinesApi
    @Suppress("DeferredResultUnused")
    fun getMovieDiscover(pageNumber: Int) {
        coroutineScope.async(exceptionHandler) {
            viewModelScope.launch(Dispatchers.IO) {
                val movieResponses = integrator.getDiscoverMovieList(pageNumber)
                val dataPack = StateLiveData.Data(movieResponses)
                discoverMutableMovies.postValue(dataPack)
            }
        }
    }

    /** coroutine movie search list**/
    private val searchMutableMovies = MutableLiveData<StateLiveData<List<ph.movieguide.com.data.vo.MovieScreen>>>()
    val stateSearch: LiveData<StateLiveData<List<ph.movieguide.com.data.vo.MovieScreen>>>
        get() = searchMutableMovies


    /** coroutine movie search **/
    @ExperimentalCoroutinesApi
    @Suppress("DeferredResultUnused")
    fun getMovieSearch(searchName: String, pageNumber: Int) {
        coroutineScope.async(exceptionHandler) {
            viewModelScope.launch(Dispatchers.IO) {
                val movieResponses = integrator.getSearchMovieList(searchName, pageNumber)
                val dataPack = StateLiveData.Data(movieResponses)
                searchMutableMovies.postValue(dataPack)
            }
        }
    }

    /** coroutine movie screen from DB **/
    private val screenMutableMovie = MutableLiveData<StateLiveData<List<MovieScreen>>>()
    val stateScreenDB: LiveData<StateLiveData<List<MovieScreen>>>
        get() = screenMutableMovie

    fun getMovieScreenFromDB() {
        coroutineScope.launch {
            val movieScreenList = integrator.getMovieScreenDBList()
            movieScreenList.collect { list ->
                list.forEach {
                    val moviesList = mapper.mapFromStorage(it)
                    movieScreenDB.add(moviesList)
                }
                val dataPack = StateLiveData.Data(movieScreenDB)
                screenMutableMovie.postValue(dataPack)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        jobCoroutine.cancel()
    }
}