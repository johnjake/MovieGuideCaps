package ph.movieguide.com.features.cinema.paging

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ph.movieguide.com.data.vo.MovieScreen
import androidx.recyclerview.widget.RecyclerView.ViewHolder as LibViewHolder

class NowShowingAdapter : ListAdapter<MovieScreen, LibViewHolder>(repositoryComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return NowShowingViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val movieItem = getItem(position)
        if (movieItem != null) {
           /** (holder as NowShowingViewHolder).bind(movieItem) **/
        }
    }

    companion object {
        private val repositoryComparator = object : DiffUtil.ItemCallback<MovieScreen>() {
            override fun areItemsTheSame(oldItem: MovieScreen, newItem: MovieScreen): Boolean =
                oldItem.title == newItem.title

            override fun areContentsTheSame(oldItem: MovieScreen, newItem: MovieScreen): Boolean =
                oldItem == newItem
        }
    }

}