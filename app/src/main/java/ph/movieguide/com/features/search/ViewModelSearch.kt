package ph.movieguide.com.features.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ph.movieguide.com.data.mapper.NowPlayingMapper
import ph.movieguide.com.data.vo.MovieScreen
import ph.movieguide.com.utils.State
import timber.log.Timber

class ViewModelSearch(
    private val integrator: Repository,
    private val mapper: NowPlayingMapper) : ViewModel() {

    private var movieScreenDB: MutableList<MovieScreen> = arrayListOf()
    private val mutableList = MutableLiveData<State<List<MovieScreen>>>()
    val stateLiveData: LiveData<State<List<MovieScreen>>> get() = mutableList

    private val jobCoroutine = Job()
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Timber.e("Error Occurred at: $throwable")
    }

    private val mutableSearch = MutableLiveData<State<List<MovieScreen>>>()
    val stateSearch: LiveData<State<List<MovieScreen>>> get() = mutableSearch

    private val coroutineScope = CoroutineScope(Dispatchers.IO + jobCoroutine + exceptionHandler)

    fun getSearchSuggestionFromDB() {
        coroutineScope.launch {
            coroutineScope.launch {
                val topMovieList = integrator.getNowPlayingMovies()
                topMovieList.collect { list ->
                    list.forEach {
                        val movie = mapper.mapFromStorage(it)
                        movieScreenDB.add(movie)
                    }
                    val dataPack = State.Data(movieScreenDB)
                    mutableList.postValue(dataPack)
                }
            }
        }
    }

    fun saveDiscoverList(list: List<MovieScreen>) {
        coroutineScope.launch {
            integrator.saveDiscoverList(list)
        }
    }

    fun getSearchResult(queryString: String) {
        coroutineScope.launch {
            val dataResult = integrator.getSearchMovies(queryString, 1)
            val dataPack = State.Data(dataResult)
            mutableSearch.postValue(dataPack)
        }
    }
}