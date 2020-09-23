package ph.movieguide.com.features.discover.adapter

import android.annotation.SuppressLint
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import ph.movieguide.com.R
import ph.movieguide.com.data.Genres


class ViewHolderFilter(override val containerView: View?): RecyclerView.ViewHolder(containerView!!),
    LayoutContainer {
    private var txtVisitedTitle: TextView = containerView?.findViewById(R.id.txtFilter)!!

    @SuppressLint("SetTextI18n")
    fun bind(movie: Genres, itemListener: FilterAdapter.OnItemClickListener, position: Int) {
        txtVisitedTitle.text = movie.name
        containerView?.setOnClickListener {
            itemListener.onItemFilter(movie, position)
        }
    }
}