package ph.movieguide.com.features.search

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ph.movieguide.android_baseplate_persistence.model.DBMovieDiscover

class SearchPagingAdapter() : PagingDataAdapter<DBMovieDiscover, ViewHolder>(COMPARATOR) {


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
        private val COMPARATOR = object : DiffUtil.ItemCallback<DBMovieDiscover>() {
            override fun areItemsTheSame(oldItem: DBMovieDiscover, newItem: DBMovieDiscover): Boolean =
                oldItem.title == newItem.title

            override fun areContentsTheSame(oldItem: DBMovieDiscover, newItem: DBMovieDiscover): Boolean =
                oldItem == newItem
        }
    }
}