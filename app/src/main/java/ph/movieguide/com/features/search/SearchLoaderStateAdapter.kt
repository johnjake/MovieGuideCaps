package ph.movieguide.com.features.search

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter

class SearchLoaderStateAdapter(
    private val retry: ()-> Unit
) : LoadStateAdapter<SearchLoaderStateViewHolder>() {

    override fun onBindViewHolder(holder: SearchLoaderStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): SearchLoaderStateViewHolder {
       return SearchLoaderStateViewHolder.getInstance(parent, retry)
    }
}