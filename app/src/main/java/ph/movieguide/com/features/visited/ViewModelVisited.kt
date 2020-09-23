package ph.movieguide.com.features.visited

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ph.movieguide.com.data.mapper.VisitedMapper
import ph.movieguide.com.data.vo.MovieScreen
import ph.movieguide.com.utils.State
import timber.log.Timber

val viewModelVisited = module {
    viewModel { ViewModelVisited(integrator = get(), mapper = get()) }
}
class ViewModelVisited(
    private val integrator: Repository,
    private val mapper: VisitedMapper
) : ViewModel() {

    /** for single request scope **/
    private val jobCoroutine = Job()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Timber.e("Error Occurred at: $throwable")
    }

    private val coroutineScope = CoroutineScope(Dispatchers.IO + jobCoroutine + exceptionHandler)

    private var movieVisitedList: MutableList<MovieScreen> = arrayListOf()

    /** coroutine movie screen from DB **/
    private val screenMutableMovie = MutableLiveData<State<List<MovieScreen>>>()
    val stateVisited: LiveData<State<List<MovieScreen>>>
        get() = screenMutableMovie

    fun getVisitedMovies() {
        coroutineScope.launch {
            val movieScreenList = integrator.getVisitedDBList()
            movieScreenList.collect { list ->
                list.forEach {
                    val moviesList = mapper.mapFromStorage(it)
                    movieVisitedList.add(moviesList)
                }
                val dataPack = State.Data(movieVisitedList)
                screenMutableMovie.postValue(dataPack)
            }
        }
    }
}