package ph.movieguide.com.features.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import ph.movieguide.com.data.mapper.MovieMapper
import ph.movieguide.com.data.vo.MovieScreen
import ph.movieguide.com.utils.State
import ph.movieguide.com.utils.schedulers.BaseSchedulerProvider
import timber.log.Timber

class ViewModelSplash(
    private val integrator: Repository,
    private val schedulers: BaseSchedulerProvider,
    private val mapper: MovieMapper
) : ViewModel() {

    private val jobCoroutine = Job()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Timber.e("Error Occurred at: $throwable")
    }

    private val coroutineScope = CoroutineScope(Dispatchers.IO + jobCoroutine + exceptionHandler)

    private val topMutableMovie = MutableLiveData<State<List<MovieScreen>>>()
    val stateTopMovie: LiveData<State<List<MovieScreen>>>
        get() = topMutableMovie

    fun getTopMovies(pageNumber: Int) {
        coroutineScope.async(exceptionHandler) {
            viewModelScope.launch(Dispatchers.IO) {
                val movieResponses = integrator.getTopRatedMovies(DEFAULT_LANGUAGE, pageNumber)
                val dataPack = State.Data(movieResponses)
                topMutableMovie.postValue(dataPack)
            }
        }
    }

    companion object {
        const val DEFAULT_LANGUAGE = "en-US"
    }


    override fun onCleared() {
        super.onCleared()
        jobCoroutine.cancel()
    }
}