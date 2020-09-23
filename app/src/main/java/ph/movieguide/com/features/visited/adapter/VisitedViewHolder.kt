package ph.movieguide.com.features.visited.adapter

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import kotlinx.android.extensions.LayoutContainer
import ph.movieguide.com.R
import ph.movieguide.com.data.vo.MovieScreen

class VisitedViewHolder(override val containerView: View?): RecyclerView.ViewHolder(containerView!!),
    LayoutContainer {
    private var txtVisitedTitle: TextView = containerView?.findViewById(R.id.txtItemVisitedTitle)!!
    private var txtVisitedRating: TextView = containerView?.findViewById(R.id.txtItemVisitedRating)!!
    private var txtVisitedDate: TextView = containerView?.findViewById(R.id.txtItemVisitedDate)!!
    private var imgView: ImageView = containerView?.findViewById(R.id.imgItemVisited)!!

    @SuppressLint("SetTextI18n")
    fun bind(movie: MovieScreen, itemListener: VisitedAdapter.OnItemClickListener, position: Int, baseUrl: String) {
        val dateRelease = movie.release_date
        txtVisitedDate.text = "Release On: $dateRelease"
        txtVisitedTitle.text = movie.title
        txtVisitedRating.text = "Rating: ${movie.vote_count}"
        imgView.load(baseUrl+movie.poster_path)
        imgView.setOnClickListener {
            itemListener.onItemClick(movie, position)
        }
        containerView?.setOnClickListener {
            itemListener.onItemClick(movie, position)
        }
    }
}