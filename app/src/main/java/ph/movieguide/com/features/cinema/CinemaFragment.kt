package ph.movieguide.com.features.cinema

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_in_cinema.*
import kotlinx.android.synthetic.main.fragment_in_cinema.cinemaPager as sliderPager
import kotlinx.android.synthetic.main.fragment_in_cinema.cinemaTab as indicators
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ph.movieguide.com.R
import ph.movieguide.com.data.mapper.NowPlayingMapper
import ph.movieguide.com.data.vo.MovieScreen
import ph.movieguide.com.features.cinema.adapter.CinemaAdapter
import ph.movieguide.com.features.cinema.adapter.SliderPagerAdapter
import ph.movieguide.com.features.cinema.paging.PagingMovieAdapter
import ph.movieguide.com.features.discover.DiscoverFragment
import ph.movieguide.com.features.main.MainActivity
import ph.movieguide.com.utils.State
import timber.log.Timber
import java.util.*
import kotlinx.android.synthetic.main.fragment_in_cinema.recycleViewTopRated as listView
import kotlinx.android.synthetic.main.fragment_in_cinema.recycle_cinema_preview as listPreview

@ExperimentalPagingApi
class CinemaFragment : Fragment(),
    CinemaAdapter.OnItemClickListener,
    PagingMovieAdapter.OnItemCinemaClick,
    SliderPagerAdapter.OnItemClickListener {
    private lateinit var navController: NavController
    private val viewModel: ViewModelCinema by viewModel()
    private val cinemaAdapter by lazy { CinemaAdapter(this) }
    private val showingAdapter by lazy { PagingMovieAdapter(this) }
    private val mapper: NowPlayingMapper = NowPlayingMapper.getInstance()
    private val sliderAdapter by lazy { context?.let { SliderPagerAdapter(it, this) } }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_in_cinema, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getTopRatedMoviesFromDB()

        val managerLayout = LinearLayoutManager(requireContext()).apply {
            orientation = LinearLayoutManager.HORIZONTAL
        }

        val gridLayoutManager = GridLayoutManager(requireContext(), 2)
        val decorationStyle = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)

        listView.apply {
            layoutManager = managerLayout
            adapter = cinemaAdapter
        }

        listPreview.apply {
            layoutManager = gridLayoutManager
            adapter = showingAdapter
            addItemDecoration(decorationStyle)
        }
        fetchNowShowing()

        search_cinema.setOnClickListener {
            it.findNavController().navigate(R.id.action_cinema_to_search)
        }

        seeAllShowing.setOnClickListener {
            it.findNavController().navigate(R.id.action_cinema_to_search)
        }

        seeAllBottom.setOnClickListener {
            it.findNavController().navigate(R.id.action_cinema_to_search)
        }

    }

    private fun fetchNowShowing() {
        lifecycleScope.launch {
            viewModel.fetchMovieNowShowing().distinctUntilChanged().collectLatest {
               showingAdapter.submitData(it.mapSync { db->mapper.mapFromStorage(db) })
            }
        }
    }

    override fun onStart() {
        super.onStart()
        observerScreenDBState()
    }

    override fun onResume() {
        super.onResume()
        bottomVisibility()
    }

    private fun bottomVisibility() {
        if(MainActivity.onBackPress) {
            MainActivity.onBackPress = false
            val bottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.bottom_nav)
            bottomNavigationView?.visibility = View.VISIBLE
        }
    }


    /** observe top rated from DB **/
    private fun observerScreenDBState() {
       viewModel.stateTopMovieDB.observe(viewLifecycleOwner, Observer { state -> handleScreenDBObserverState(state) })
    }

    /** observer state database **/
    private fun handleScreenDBObserverState(state: State<List<MovieScreen>>) {
        when (state) {
            is State.Data -> handleScreenDBSuccess(state.data)
            is State.Error -> handleScreenDBFailed(state.error)
        }
    }

    /** success get data from db **/
    private fun handleScreenDBSuccess(dataList: List<MovieScreen>) {
        cinemaAdapter.dataSource = dataList
        initSlider(dataList.take(5))
    }

    private fun handleScreenDBFailed(error: Throwable) {
        Timber.e("Error $error")
    }

    //Top Movies
    override fun onItemClick(movie: MovieScreen, position: Int) {
        MainActivity.onTopDetails = true
        MainActivity.onDiscoverDetails = false
        MainActivity.onNowShowingDetails = false
        val actionByParam = CinemaFragmentDirections.actionCinemaToDetails(movie.id)
        findNavController().navigate(actionByParam)
    }

    //Now Showing Details
    override fun onItemClickListener(movieScreen: MovieScreen) {
        MainActivity.onTopDetails = false
        MainActivity.onDiscoverDetails = false
        MainActivity.onNowShowingDetails = true
        val actionByParam = CinemaFragmentDirections.actionCinemaToDetails(movieScreen.id)
        findNavController().navigate(actionByParam)
    }

    override fun onItemSliderClick(movieId: Int) {
        MainActivity.onTopDetails = true
        MainActivity.onDiscoverDetails = false
        MainActivity.onNowShowingDetails = false
        val actionByParam = CinemaFragmentDirections.actionCinemaToDetails(movieId)
        findNavController().navigate(actionByParam)
    }

    private fun initSlider(list: List<MovieScreen>) {
        sliderAdapter?.dataSource = list
        sliderPager.adapter = sliderAdapter
        val timer = Timer()
        timer.scheduleAtFixedRate(activity?.let {
            DiscoverFragment.SliderTimer(
                it,
                sliderPager,
                list
            )
        }, 4000, 6000)
        indicators.setupWithViewPager(sliderPager, true)
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

    companion object{
        private const val ARG_CAUGHT = "CinemaFragment"
        fun newInstance(caught: String): CinemaFragment {
            val args: Bundle = Bundle()
            args.putSerializable(ARG_CAUGHT, caught)
            val fragment = CinemaFragment()
            fragment.arguments = args
            return fragment
        }
    }


}