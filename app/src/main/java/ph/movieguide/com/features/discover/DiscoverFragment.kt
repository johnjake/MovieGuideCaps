package ph.movieguide.com.features.discover

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.fragment_discover.listView as viewList
import kotlinx.android.synthetic.main.fragment_discover.listFilter as filterList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel
import ph.movieguide.com.R
import ph.movieguide.com.data.Genres
import ph.movieguide.com.data.vo.MovieScreen
import ph.movieguide.com.features.discover.adapter.DiscoverAdapter
import ph.movieguide.com.features.discover.adapter.FilterAdapter
import ph.movieguide.com.features.discover.adapter.SliderPagerAdapter
import ph.movieguide.com.features.main.MainActivity.Companion.DISCOVER_MOVIES
import ph.movieguide.com.utils.GenreList
import ph.movieguide.com.utils.State
import timber.log.Timber
import java.util.*
import kotlinx.android.synthetic.main.fragment_discover.indicator as indicators
import kotlinx.android.synthetic.main.fragment_discover.slider_pager as sliderPager

class DiscoverFragment : Fragment(), DiscoverAdapter.OnItemClickListener, FilterAdapter.OnItemClickListener {

    private val viewModel: ViewModelDiscover by viewModel()

    private val sliderAdapter by lazy { context?.let { SliderPagerAdapter(it) } }

    private val discoverAdapter by lazy { DiscoverAdapter(this) }

    private val filterAdapter by lazy { FilterAdapter(this) }

    private val mutableList: MutableList<MovieScreen> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_discover, container, false)

    @Suppress("UNCHECKED_CAST")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val listMovies = arguments?.getSerializable(DISCOVER_MOVIES) as List<MovieScreen>
        observerDiscoverState()
       // observerFilterState()
        initSlider(listMovies.take(5))
        initAdapter()
    }

    @ExperimentalCoroutinesApi
    override fun onStart() {
        super.onStart()
        viewModel.getSearchResult()
        //viewModel.getFilterResult()
    }

    private fun initSlider(list: List<MovieScreen>) {
        sliderAdapter?.dataSource = list
        sliderPager.adapter = sliderAdapter
        val timer = Timer()
        timer.scheduleAtFixedRate(activity?.let { SliderTimer(it, sliderPager, list) }, 4000, 6000)
        indicators.setupWithViewPager(sliderPager, true)
    }

    private fun initAdapter() {
        val managerLayout = LinearLayoutManager(requireContext()).apply {
            orientation = LinearLayoutManager.HORIZONTAL
        }

        val managerFilter = LinearLayoutManager(requireContext()).apply {
            orientation = LinearLayoutManager.HORIZONTAL
        }

        val gridLayoutManager = GridLayoutManager(requireContext(), 2)
        val decorationStyle = DividerItemDecoration(requireContext(), DividerItemDecoration.HORIZONTAL)

        filterList.apply {
            layoutManager = managerFilter
            adapter = filterAdapter
            addItemDecoration(decorationStyle)
        }

        viewList.apply {
            layoutManager = managerLayout
           adapter = discoverAdapter
        }

        val genreList = GenreList()
        filterAdapter.dataSource = genreList.genreInstance()

    }

    /** observe state discover **/
    private fun observerDiscoverState() {
        viewModel.stateDiscover.observe(viewLifecycleOwner, Observer { state -> handleDiscoverObserverState(state) })
    }

    /** observe state discover **/
    private fun observerFilterState() {
        viewModel.stateFilter.observe(viewLifecycleOwner, Observer { state -> handleFilterObserverState(state) })
    }

    /** observer state discover **/
    private fun handleDiscoverObserverState(state: State<List<MovieScreen>>) {
        when (state) {
            is State.Data -> handleDiscoverSuccess(state.data)
            is State.Error -> handleDiscoverFailed(state.error)
        }
    }

    /** observer state discover **/
    private fun handleFilterObserverState(state: State<List<MovieScreen>>) {
        when (state) {
            is State.Data -> handleFilterSuccess(state.data)
            is State.Error -> handleFilterFailed(state.error)
        }
    }

    private fun handleFilterFailed(error: Throwable) {
        Timber.e("$error")
    }

    private fun handleFilterSuccess(list: List<MovieScreen>) {

    }

    private fun handleDiscoverSuccess(discoveryList: List<MovieScreen>) {
        if(discoveryList.isNotEmpty()) {
            discoverAdapter.dataSource = discoveryList
            mutableList.addAll(discoveryList)
        }

    }

    private fun handleDiscoverFailed(error: Throwable) {
        Timber.e("$error")
    }

    companion object{
        private const val ARG_CAUGHT = "DiscoverFragment"
        fun newInstance(caught: String):DiscoverFragment{
            val args: Bundle = Bundle()
            args.putSerializable(ARG_CAUGHT, caught)
            val fragment = DiscoverFragment()
            fragment.arguments = args
            return fragment
        }
    }

    internal class SliderTimer(private val activity: Activity,
                               private val sliderPager: ViewPager,
                               private val lstSlider: List<MovieScreen>) : TimerTask() {
        override fun run() {
            activity.runOnUiThread(Runnable {
                if (sliderPager.currentItem < lstSlider.size - 1) {
                    sliderPager.currentItem = sliderPager.currentItem + 1
                } else sliderPager.currentItem = 0
            })
        }
    }

    override fun onItemClick(movie: MovieScreen, position: Int) {
        Toast.makeText(context, "Movie Title ${movie.title}", Toast.LENGTH_SHORT).show()
    }

    override fun onItemFilter(genres: Genres, position: Int) {
         Toast.makeText(context, "${genres.name}", Toast.LENGTH_SHORT).show()
     }
}