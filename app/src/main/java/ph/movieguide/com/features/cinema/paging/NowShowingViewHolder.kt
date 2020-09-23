package ph.movieguide.com.features.cinema.paging

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.makeramen.roundedimageview.RoundedImageView
import ph.movieguide.com.BuildConfig
import ph.movieguide.com.R
import ph.movieguide.com.data.vo.MovieScreen
import timber.log.Timber


class NowShowingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val movieId: TextView = view.findViewById(R.id.textViewId)
    private val movieTitle:TextView = view.findViewById(R.id.txtMovieTitle)
    private val movieRelease: TextView = view.findViewById(R.id.txtTitle)
    private val imgPoster: RoundedImageView = view.findViewById(R.id.imgView)
    private val baseUrl = BuildConfig.BASE_URL_POSTER
    private var repo: MovieScreen? = null

    init {
        view.setOnClickListener {
            repo?.title?.let { url ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                view.context.startActivity(intent)
            }
        }
    }

    fun bind(movieScreen: MovieScreen?, listener: PagingMovieAdapter.OnItemCinemaClick) {
        if (movieScreen == null) {

        } else {
            showRepoData(movieScreen)
            imgPoster.setOnClickListener {
                 listener.onItemClickListener(movieScreen)
            }
        }
    }


    @SuppressLint("SetTextI18n")
    private fun showRepoData(movieScreen: MovieScreen) {
        val dateRelease = movieScreen.release_date
        movieRelease.text = "Showing On: $dateRelease"
        movieId.text = movieScreen.id.toString()
        movieTitle.text = movieScreen.title
        imgPoster.load(baseUrl+ movieScreen.poster_path)
    }

    companion object {
        fun create(parent: ViewGroup): NowShowingViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_now_showing, parent, false)
            return NowShowingViewHolder(view)
        }
    }
}