package ph.movieguide.com.features.search

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ph.movieguide.com.BuildConfig
import ph.movieguide.com.data.vo.MovieScreen

class SearchPagingAdapter() : PagingDataAdapter<MovieScreen, ViewHolder>(COMPARATOR) {


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        if(item != null) {
            (holder as SearchPagingViewHolder).bind(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchPagingViewHolder {
        return SearchPagingViewHolder.create(parent)
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<MovieScreen>() {
            override fun areItemsTheSame(oldItem: MovieScreen, newItem: MovieScreen): Boolean =
                oldItem.title == newItem.title

            override fun areContentsTheSame(oldItem: MovieScreen, newItem: MovieScreen): Boolean =
                oldItem == newItem
        }
    }
}