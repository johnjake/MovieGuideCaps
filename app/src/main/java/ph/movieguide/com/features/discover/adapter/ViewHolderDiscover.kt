package ph.movieguide.com.features.discover.adapter

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import kotlinx.android.extensions.LayoutContainer
import ph.movieguide.com.R
import ph.movieguide.com.data.vo.MovieScreen

class ViewHolderDiscover(override val containerView: View?): RecyclerView.ViewHolder(containerView!!),
    LayoutContainer {
    private var txtVisitedTitle: TextView = containerView?.findViewById(R.id.textDiscoverTitle)!!
    private var imgView: ImageView = containerView?.findViewById(R.id.imgDiscover)!!

    @SuppressLint("SetTextI18n")
    fun bind(movie: MovieScreen, itemListener: DiscoverAdapter.OnItemClickListener, position: Int, baseUrl: String) {
        txtVisitedTitle.text = movie.title
        imgView.load(baseUrl+movie.poster_path)
        imgView.setOnClickListener {
            itemListener.onItemClick(movie, position)
        }
        containerView?.setOnClickListener {
            itemListener.onItemClick(movie, position)
        }
    }
}