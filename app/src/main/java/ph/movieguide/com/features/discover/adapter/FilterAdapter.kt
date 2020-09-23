package ph.movieguide.com.features.discover.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ph.movieguide.com.R
import ph.movieguide.com.data.Genres
import ph.movieguide.com.data.vo.MovieScreen

class FilterAdapter(
    private val itemListener: OnItemClickListener
) : RecyclerView.Adapter<ViewHolderFilter>() {

    interface OnItemClickListener {
        fun onItemFilter(movie: Genres, position: Int)
    }

    private var dataSources: List<Genres> = emptyList()

    var dataSource: List<Genres>
        get() = dataSources
        set(value) {
            dataSources = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderFilter {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolderFilter(inflater.inflate(R.layout.item_filter, parent, false))
    }

    override fun getItemCount(): Int = dataSources.size

    override fun onBindViewHolder(holder: ViewHolderFilter, position: Int) {
        holder.bind(dataSources[position], itemListener, position)
    }
}