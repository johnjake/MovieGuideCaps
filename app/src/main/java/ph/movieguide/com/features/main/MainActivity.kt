package ph.movieguide.com.features.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.paging.ExperimentalPagingApi
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import ph.movieguide.com.R
import ph.movieguide.com.data.mapper.DiscoverMapper
import ph.movieguide.com.data.vo.Discover
import ph.movieguide.com.data.vo.MovieScreen
import ph.movieguide.com.features.cinema.CinemaFragment
import ph.movieguide.com.features.discover.DiscoverFragment
import ph.movieguide.com.features.visited.VisitedFragment
import ph.movieguide.com.utils.State
import timber.log.Timber
import java.io.Serializable


class MainActivity : AppCompatActivity() {

    companion object {
        /** For Display Details  as it search to local database **/
        var onBackPress: Boolean = false
        var onTopDetails: Boolean = false
        var onNowShowingDetails: Boolean = false
        var onDiscoverDetails: Boolean = false
        const val DISCOVER_MOVIES = "discover_movies_list"
    }

    private var bundle = Bundle()
    private val viewModel: ViewModelMain by viewModel()

    private lateinit var bottomNavView: BottomNavigationView
    @ExperimentalPagingApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // handleObserverDetails()
         observerDiscoverState()
        // observerSearchState()
        // observerScreenDBState()
        initializedUI()
    }

    @ExperimentalCoroutinesApi
    override fun onStart() {
        super.onStart()
        viewModel.getMovieDiscover(1)
    }

    @ExperimentalPagingApi
    private fun initializedUI() {
        setSupportActionBar(toolbar)
        setupBottomNavMenu()
    }

    @ExperimentalPagingApi
    private fun setupBottomNavMenu(){
        bottomNavView = findViewById<BottomNavigationView>(R.id.bottom_nav).apply {
            itemIconTintList = null
        }
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        bottomNavView.setOnNavigationItemSelectedListener(object :
            OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(@NonNull item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.destination_movies -> {
                        openFragment(CinemaFragment.newInstance("Cinema Fragment"))
                        return true
                    }
                    R.id.destination_discover -> {
                        val instanceDiscover = DiscoverFragment.newInstance("Discover Fragment")
                        instanceDiscover.arguments = bundle
                        openFragment(instanceDiscover)
                        return true
                    }
                    R.id.destination_visited -> {
                        openFragment(VisitedFragment.newInstance("Visited Fragment"))
                        return true
                    }
                }
                return true
            }
        })
    }

    fun openFragment(fragment: Fragment) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.nav_host_fragment, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return true
    }

    override fun onResume() {
        super.onResume()
    }

    /** observe state discover **/
    private fun observerDiscoverState() {
        viewModel.stateDiscover.observe(
            this,
            Observer { state -> handleDiscoverObserverState(state) })
    }

    /** observer state discover **/
    private fun handleDiscoverObserverState(state: State<List<MovieScreen>>) {
        when (state) {
            is State.Data -> handleDiscoverSuccess(state.data)
            is State.Error -> handleDiscoverFailed(state.error)
        }
    }

    private fun handleDiscoverSuccess(discoveryList: List<MovieScreen>) {
       val serializeMap = discoveryList as Serializable
        bundle.putSerializable(DISCOVER_MOVIES, serializeMap);
    }

    private fun handleDiscoverFailed(error: Throwable) {
        Timber.e("$error")
    }

    /** private val viewModel: ViewModel by viewModels<ViewModel> { viewModelFactory }
    /** observer state details **/
    private fun handleObserverState(state: StateLiveData<MovieDetails?>) {
        when (state) {
            is StateLiveData.Data -> state.data?.let { handleDetailsSuccess(it) }
            is StateLiveData.Error -> handleDetailsFailed(state.error)
        }
    }

    /** observer state search **/
    private fun handleSearchObserverState(state: StateLiveData<List<MovieScreen>>) {
        when (state) {
            is StateLiveData.Data -> handleSearchSuccess(state.data)
            is StateLiveData.Error -> handleSearchFailed(state.error)
        }
    }

    /** observer state database **/
    private fun handleScreenDBObserverState(state: StateLiveData<List<MovieScreen>>) {
        when (state) {
            is StateLiveData.Data -> handleScreenDBSuccess(state.data)
            is StateLiveData.Error -> handleScreenDBFailed(state.error)
        }
    }

    /** observe state details**/
    private fun observerState() {
        viewModel.stateMovie.observe(this, Observer { state -> handleObserverState(state) })
    }



    /** observe state search **/
    private fun observerSearchState() {
        viewModel.stateSearch.observe(this, Observer { state -> handleSearchObserverState(state) })
    }

    /** observe state search **/
    private fun observerScreenDBState() {
        viewModel.stateScreenDB.observe(this, Observer { state -> handleScreenDBObserverState(state) })
    }

    @ExperimentalCoroutinesApi
    override fun initialized() {
        setContentView(R.layout.activity_main)
        handleObserverDetails()
        observerDiscoverState()
        observerSearchState()
        observerScreenDBState()
        /** helloWorld.setOnClickListener {
            viewModel.getMovieScreenFromDB()
        } **/
    }

    override fun onStart() {
        super.onStart()
        viewModel.getMovieScreenFromDB()
    }

    private fun handleObserverDetails() {
        viewModel.state.observe(this, Observer { state ->
             when(state) {
                 is RxState.MovieDetailsSuccess -> handleDetailsSuccess(state.movieDetails)
             }
        })
    }

    private fun handleDetailsSuccess(details: MovieDetails) {
       toast("${details.title}")
        val blocker  = 0
    }

    private fun handleDetailsFailed(error: Throwable) {
        Timber.e("$error")
    }



    private fun handleSearchSuccess(searchListResult: List<MovieScreen>) {
        val data = searchListResult
        val blocker = 0
    }

    private fun handleSearchFailed(error: Throwable) {
        Timber.e("$error")
    }

    private fun handleScreenDBSuccess(searchListResult: List<MovieScreen>) {
        val data = searchListResult
        val blocker = 0
    }

    private fun handleScreenDBFailed(error: Throwable) {
        Timber.e("$error")
    } **/
}