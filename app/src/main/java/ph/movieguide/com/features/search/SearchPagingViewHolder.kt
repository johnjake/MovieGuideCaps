package ph.movieguide.com.features.search

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import ph.movieguide.com.R
import ph.movieguide.com.data.vo.MovieScreen

class SearchPagingViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    private val baseUrl: String = ph.movieguide.com.BuildConfig.BASE_URL_POSTER
    private var textView: TextView = view.findViewById(R.id.txtSearchTitle)
    private var textRelease: TextView = view.findViewById(R.id.txtSearchRelease)
    private var movieId: TextView = view.findViewById(R.id.txtMovieId)
    private var imgView: ImageView = view.findViewById(R.id.imgSearch)
    private var ratingBarSearch: RatingBar = view.findViewById(R.id.ratingBarSearch)

    @SuppressLint("SetTextI18n")
    fun bind(movie: MovieScreen) {
        val rating = movie.vote_average/2
        ratingBarSearch.rating = rating.toFloat()
        movieId.text = movie.id.toString()
        textRelease.text = "(${movie.vote_average})"
        textView.text = movie.title
        if (!movie.backdrop_path.isNullOrEmpty()) {
            imgView.setPadding(5,5,5,5,)
            setImageStrokeDrawable(R.drawable.stroke_bright_pink_avatar)
            imgView.load(baseUrl+movie.backdrop_path)
        } else {
            setImageStrokeDrawable(R.drawable.stroke_gray_avatar)
            imgView.scaleType = ImageView.ScaleType.CENTER_CROP
            imgView.load(R.drawable.ic_movie_empty)
            imgView.setPadding(5,5,5,5,)
        }

    }
    private fun setImageStrokeDrawable(@DrawableRes id: Int) {
        imgView.background = ResourcesCompat.getDrawable(view.resources, id, null)
    }

    companion object {
        fun create(parent: ViewGroup): SearchPagingViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_search_view, parent, false)
            return SearchPagingViewHolder(view = view)
        }
    }

}