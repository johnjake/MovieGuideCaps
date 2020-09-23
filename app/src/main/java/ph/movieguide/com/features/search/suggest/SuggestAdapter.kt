package ph.movieguide.com.features.search.suggest

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ph.movieguide.com.R
import ph.movieguide.com.data.vo.MovieScreen
import ph.movieguide.com.di.providesBaseUrlImagePoster

class SuggestAdapter(
    private val itemListener: OnItemClickListener
) : RecyclerView.Adapter<SuggestViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(movie: MovieScreen, position: Int)
    }

    private val baseUrl: String = providesBaseUrlImagePoster()

    private var dataSources: List<MovieScreen> = emptyList()

    var dataSource: List<MovieScreen>
        get() = dataSources
        set(value) {
            dataSources = value.take(10)
            notifyDataSetChanged()
        }

    val blocker = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuggestViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return SuggestViewHolder(inflater.inflate(R.layout.item_search_view, parent, false))
    }

    override fun onBindViewHolder(holder: SuggestViewHolder, position: Int) {
        holder.bind(dataSources[position], itemListener, position, baseUrl)
    }

    override fun getItemCount(): Int = dataSources.size
}