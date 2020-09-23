package ph.movieguide.com.features.discover

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
import ph.movieguide.com.data.mapper.DiscoverMapper
import ph.movieguide.com.data.vo.MovieScreen
import ph.movieguide.com.utils.State
import timber.log.Timber

val searchDiscover = module {
    viewModel { ViewModelDiscover(integrator = get(), mapper = get()) }
}
class ViewModelDiscover(private val integrator: Repository,
                        private val mapper: DiscoverMapper
) : ViewModel() {
    /** for single request scope **/
    private val jobCoroutine = Job()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Timber.e("Error Occurred at: $throwable")
    }

    private val coroutineScope = CoroutineScope(Dispatchers.IO + jobCoroutine + exceptionHandler)
    private var searchDiscoverList: MutableList<MovieScreen> = arrayListOf()
    private var filterList: MutableList<MovieScreen> = arrayListOf()

    /** coroutine movie screen from DB **/
    private val screenMutableMovie = MutableLiveData<State<List<MovieScreen>>>()
    val stateDiscover: LiveData<State<List<MovieScreen>>>
        get() = screenMutableMovie

    private val mutableFilter = MutableLiveData<State<List<MovieScreen>>>()
    val stateFilter: LiveData<State<List<MovieScreen>>> get() = mutableFilter


    fun getSearchResult() {
        coroutineScope.launch {
            val flowList = integrator.getDiscoverMovies()
             flowList.collect { list ->
                 list.forEach {
                     val mapList = mapper.mapFromStorage(it)
                     searchDiscoverList.add(mapList)
                 }
                 val dataPack = State.Data(searchDiscoverList)
                 screenMutableMovie.postValue(dataPack)
             }
        }
    }

    fun getFilterResult() {
        coroutineScope.launch {
            val flowList = integrator.getDiscoverMovies()
            flowList.collect { list ->
                list.forEach {
                    val mapList = mapper.mapFromStorage(it)
                    filterList.add(mapList)
                }
                val dataPack = State.Data(filterList)
                mutableFilter.postValue(dataPack)
            }
        }
    }
}