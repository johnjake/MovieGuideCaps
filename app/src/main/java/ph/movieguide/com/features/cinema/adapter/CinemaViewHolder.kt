package ph.movieguide.com.features.cinema.adapter


import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import kotlinx.android.extensions.LayoutContainer
import ph.movieguide.com.R
import ph.movieguide.com.data.vo.MovieScreen

class CinemaViewHolder(override val containerView: View?): RecyclerView.ViewHolder(containerView!!),
    LayoutContainer {
    private var textView: TextView = containerView?.findViewById(R.id.textViewTopRated)!!
    private var imgView: ImageView = containerView?.findViewById(R.id.imgTopRated)!!

    @SuppressLint("SetTextI18n")
    fun bind(movie: MovieScreen, itemListener: CinemaAdapter.OnItemClickListener, position: Int, baseUrl: String) {
        val dateRelease = movie.release_date
        textView.text = "Release On: $dateRelease"
        setImageStrokeDrawable(R.drawable.stroke_gray_avatar)
        imgView.load(baseUrl+movie.poster_path)
        imgView.setOnClickListener {
            itemListener.onItemClick(movie, position)
        }
    }

    private fun setImageStrokeDrawable(@DrawableRes id: Int) {
        imgView.background =
            containerView?.resources?.let { ResourcesCompat.getDrawable(it, id, null) }
    }
}