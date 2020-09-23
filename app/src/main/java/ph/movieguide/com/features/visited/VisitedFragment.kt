package ph.movieguide.com.features.visited

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_visited.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import ph.movieguide.com.BuildConfig
import ph.movieguide.com.R
import ph.movieguide.com.data.vo.MovieScreen
import ph.movieguide.com.di.providesSharedGenres


import ph.movieguide.com.features.visited.adapter.VisitedAdapter
import ph.movieguide.com.utils.DBTypeConverter
import ph.movieguide.com.utils.State
import ph.movieguide.com.data.Genres
import ph.movieguide.com.di.providesGson

class VisitedFragment : Fragment(), VisitedAdapter.OnItemClickListener {

    private val viewModel: ViewModelVisited by viewModel()
    private val visitedAdapter by lazy { VisitedAdapter(this) }
    private val typeConvert = DBTypeConverter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_visited, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        visitedObserver()

        val managerLayout = LinearLayoutManager(requireContext()).apply {
            orientation = LinearLayoutManager.VERTICAL
        }

        listVisited.apply {
            layoutManager = managerLayout
            adapter = visitedAdapter
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.getVisitedMovies()
    }

    private fun visitedObserver() {
        viewModel.stateVisited.observe(viewLifecycleOwner, Observer { state -> handleScreenDBObserverState(state) })
    }

    /** observer state database **/
    private fun handleScreenDBObserverState(state: State<List<MovieScreen>>) {
        when (state) {
            is State.Data -> handleScreenDBSuccess(state.data)
            is State.Error -> handleScreenDBFailed(state.error)
        }
    }

    private fun handleScreenDBFailed(error: Throwable) {
        Toast.makeText(context, "${error.message}", Toast.LENGTH_SHORT).show()
    }

    private fun handleScreenDBSuccess(list: List<MovieScreen>) {
        visitedAdapter.dataSource = list
        if(list.isNotEmpty()) {
          val movie = list[0]
            movieInformation(movie)
        }
    }

    override fun onItemClick(movie: MovieScreen, position: Int) {
        movieInformation(movie)
    }

    private fun movieInformation(movie: MovieScreen) {
        val baseUrl = BuildConfig.BASE_URL_POSTER
        txtOverview.text = movie.overview
        txtRatingTrack.text = movie.vote_average.toString()
        txtVisitTitle.text = movie.title
        imgVisited.load(baseUrl+movie.poster_path)
        movie.genre_ids.forEach {
        }
    }

    companion object{
        private const val ARG_CAUGHT = "Visited Fragment"
        fun newInstance(caught: String): VisitedFragment {
            val args: Bundle = Bundle()
            args.putSerializable(ARG_CAUGHT, caught)
            val fragment = VisitedFragment()
            fragment.arguments = args
            return fragment
        }
    }
}