package ph.movieguide.com.features.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import org.koin.androidx.viewmodel.ext.android.viewModel
import ph.movieguide.com.R
import ph.movieguide.com.data.vo.MovieScreen
import ph.movieguide.com.features.main.MainActivity
import ph.movieguide.com.utils.State
import timber.log.Timber

class SplashScreenActivity : AppCompatActivity() {

   private val viewModel: ViewModelSplash by viewModel()

    private lateinit var handler: Handler

    private var runnable: Runnable? = null

    /** observer state TopMovies **/
    private fun handleObserverState(state: State<List<MovieScreen>>) {
        when (state) {
            is State.Data -> handleTopMovieSuccess(state.data)
            is State.Error -> handleTopMovieFailed(state.error)
        }
    }

    /** observe state TopMovies **/
    private fun observerState() {
        viewModel.stateTopMovie.observe(this, Observer { state -> handleObserverState(state) })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        observerState()
        runnable = Runnable {
            viewModel.getTopMovies(1)
        }
    }

    private fun handleTopMovieSuccess(topList: List<MovieScreen>) {
        if(topList.isNotEmpty()) {
            startActivity(Intent(this, MainActivity::class.java).apply {
                putExtra("INTERNET", "1")
            })
        }
    }

    private fun handleTopMovieFailed(error: Throwable) {
        Timber.e("################# Error: ${error.message}")
    }

    override fun onStart() {
        super.onStart()
        handler = Handler().apply { postDelayed(runnable, 3000) }
    }

    override fun onStop() {
        super.onStop()
        handler.removeCallbacks(null)
    }
}