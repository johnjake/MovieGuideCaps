package ph.movieguide.com.features.search.result

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import kotlinx.android.extensions.LayoutContainer
import ph.movieguide.com.R
import ph.movieguide.com.data.vo.MovieScreen

class ResultViewHolder(override val containerView: View?): RecyclerView.ViewHolder(containerView!!),
    LayoutContainer {
    private var textView: TextView = containerView?.findViewById(R.id.txtSearchTitle)!!
    private var textRelease: TextView = containerView?.findViewById(R.id.txtSearchRelease)!!
    private var movieId: TextView = containerView?.findViewById(R.id.txtMovieId)!!
    private var imgView: ImageView = containerView?.findViewById(R.id.imgSearch)!!
    private var ratingBarSearch: RatingBar = containerView?.findViewById(R.id.ratingBarSearch)!!

    @SuppressLint("SetTextI18n")
    fun bind(movie: MovieScreen, itemListener: ResultAdapter.OnItemResultClickListener, position: Int, baseUrl: String) {
        val rating = movie.vote_average/2
        ratingBarSearch.rating = rating.toFloat()
        movieId.text = movie.id.toString()
        textRelease.text = "(${movie.vote_average})"
        textView.text = movie.title
        imgView.load(baseUrl+movie.backdrop_path)
        imgView.setOnClickListener {
            itemListener.onItemResultListener(movie, position)
        }
    }
}