package ph.movieguide.com.features.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ph.movieguide.com.data.vo.MovieScreen
import ph.movieguide.com.utils.State

val viewModelDataDetails = module {
    viewModel { ViewModelDetails(integrator = get()) }
}
class ViewModelDetails(
    private val integrator: Repository
) : ViewModel() {

    private val mutableTop = MutableLiveData<State<MovieScreen>>()
    val stateTop: LiveData<State<MovieScreen>> get() = mutableTop

    private val mutableMovie = MutableLiveData<State<MovieScreen>>()
    val stateMovie: LiveData<State<MovieScreen>>
        get() = mutableMovie

    private val mutableDiscover = MutableLiveData<State<MovieScreen>>()
    val stateDiscover: LiveData<State<MovieScreen>>
        get() = mutableDiscover

    fun getMovieDetails(movieId: Int) {
        viewModelScope.launch {
            val data = integrator.getMovieDetailsById(movieId)
            val dataPack = State.Data(data)
            mutableMovie.postValue(dataPack)
        }
    }

    fun getTopRatedDetails(movieId: Int) {
        viewModelScope.launch {
            val data = integrator.getTopRatedMovieById(movieId)
            val dataStack = State.Data(data)
            mutableTop.postValue(dataStack)
        }
    }

    fun getDiscoverDetails(movieId: Int) {
        viewModelScope.launch {
            val data = integrator.getDiscoverMovieById(movieId)
            val dataPack = State.Data(data)
            mutableDiscover.postValue(dataPack)
        }
    }

    fun insertMovieVisited(movieScreen: MovieScreen) {
        viewModelScope.launch {
            integrator.insertMovie(movieScreen)
        }
    }
}