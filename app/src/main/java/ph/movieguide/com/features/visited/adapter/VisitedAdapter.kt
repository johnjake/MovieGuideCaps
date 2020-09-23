package ph.movieguide.com.features.visited.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ph.movieguide.com.R
import ph.movieguide.com.data.vo.MovieScreen
import ph.movieguide.com.di.providesBaseUrlImagePoster


class VisitedAdapter(
    private val itemListener: OnItemClickListener
) : RecyclerView.Adapter<VisitedViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(movie: MovieScreen, position: Int)
    }

    private val baseUrl: String = providesBaseUrlImagePoster()

    private var dataSources: List<MovieScreen> = emptyList()

    var dataSource: List<MovieScreen>
        get() = dataSources
        set(value) {
            dataSources = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VisitedViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return VisitedViewHolder(inflater.inflate(R.layout.item_movie_visted, parent, false))
    }

    override fun onBindViewHolder(holder: VisitedViewHolder, position: Int) {
        holder.bind(dataSources[position], itemListener, position, baseUrl)
    }

    override fun getItemCount(): Int = dataSources.size
}