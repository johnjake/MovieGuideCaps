package ph.movieguide.com.features.cinema.paging

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import ph.movieguide.com.data.vo.MovieScreen

class PagingMovieAdapter(
    private val listener: OnItemCinemaClick
) : PagingDataAdapter<MovieScreen, NowShowingViewHolder>(repositoryComparator) {

    companion object {
        private val repositoryComparator = object : DiffUtil.ItemCallback<MovieScreen>() {
            override fun areItemsTheSame(oldItem: MovieScreen, newItem: MovieScreen): Boolean =
                oldItem.title == newItem.title

            override fun areContentsTheSame(oldItem: MovieScreen, newItem: MovieScreen): Boolean =
                oldItem == newItem
        }
    }

    interface OnItemCinemaClick {
        fun onItemClickListener(movieScreen: MovieScreen)
    }

    override fun onBindViewHolder(holder: NowShowingViewHolder, position: Int) {
        (holder as? NowShowingViewHolder)?.bind(getItem(position), listener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NowShowingViewHolder {
        return NowShowingViewHolder.create(parent)
    }

}