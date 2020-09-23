package ph.movieguide.com.features.details

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import coil.load
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.fragment_details.*
import kotlinx.android.synthetic.main.toolbar_movie_details.*
import ph.movieguide.com.R
import org.koin.androidx.viewmodel.ext.android.viewModel
import ph.movieguide.com.BuildConfig
import ph.movieguide.com.data.vo.MovieScreen
import ph.movieguide.com.features.main.MainActivity
import ph.movieguide.com.utils.OnBackClickListener
import ph.movieguide.com.utils.State

class MovieDetailsFragment : Fragment() {

    private val viewModel: ViewModelDetails by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_details, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNavigationView?.visibility = View.GONE

        movieDataObserver()
        movieTopObserver()
     //   movieDiscoverObserver()

        arguments?.let {
            val args = MovieDetailsFragmentArgs.fromBundle(it)
            when {
                MainActivity.onNowShowingDetails -> {
                    MainActivity.onTopDetails = false
                    MainActivity.onDiscoverDetails = false
                    viewModel.getMovieDetails(args.movieId)
                }
                MainActivity.onTopDetails -> {
                    MainActivity.onNowShowingDetails = false
                    MainActivity.onDiscoverDetails = false
                    viewModel.getTopRatedDetails(args.movieId)
                }
                MainActivity.onDiscoverDetails -> {
                    MainActivity.onNowShowingDetails = false
                    MainActivity.onTopDetails = false
                    viewModel.getDiscoverDetails(args.movieId)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        toolbar_back.setOnClickListener {
            MainActivity.onBackPress = true
            MainActivity.onTopDetails = false
            MainActivity.onNowShowingDetails = false
            this.findNavController().popBackStack()
        }
    }

    private fun movieTopObserver() {
        viewModel.stateTop.observe(viewLifecycleOwner, Observer { state -> handleMovieDetails(state)  })
    }

    private fun movieDataObserver() {
        viewModel.stateMovie.observe(viewLifecycleOwner, Observer { state -> handleMovieDetails(state)  })
    }

    private fun movieDiscoverObserver() {
        viewModel.stateDiscover.observe(viewLifecycleOwner, Observer { state -> handleMovieDetails(state)  })
    }

    private fun handleMovieDetails(state: State<MovieScreen>) {
        when (state) {
            is State.Data -> handleSuccess(state.data)
            is State.Error -> handleFailed(state.error)
        }
    }

    private fun handleFailed(error: Throwable) {
        Log.e("Error", "${error.message}")
    }

    private fun handleSuccess(movie: MovieScreen) {
        handleMovies(movie)
    }

    private fun handleMovies(movie: MovieScreen) {
        val baseUrlPoster = BuildConfig.BASE_URL_POSTER
        imgDetail.load(baseUrlPoster+movie.backdrop_path)
        txtDetailTitle.text = movie.title
        txtDetailsShowing.text = movie.release_date
        txtDetailsRating.text = movie.vote_average.toString()
        txtDetailSypnosis.text = movie.overview
        ratingBar.rating = movie.vote_average.toFloat()/2
        viewModel.insertMovieVisited(movie)
    }
}