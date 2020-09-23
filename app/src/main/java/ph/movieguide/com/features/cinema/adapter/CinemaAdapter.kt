package ph.movieguide.com.features.cinema.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ph.movieguide.com.R
import ph.movieguide.com.data.vo.MovieScreen
import ph.movieguide.com.di.providesBaseUrlImagePoster

class CinemaAdapter(
    private val itemListener: OnItemClickListener
) : RecyclerView.Adapter<CinemaViewHolder>() {

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CinemaViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CinemaViewHolder(inflater.inflate(R.layout.item_top_rated_movies, parent, false))
    }

    override fun onBindViewHolder(holder: CinemaViewHolder, position: Int) {
        holder.bind(dataSources[position], itemListener, position, baseUrl)
    }

    override fun getItemCount(): Int = dataSources.size
}