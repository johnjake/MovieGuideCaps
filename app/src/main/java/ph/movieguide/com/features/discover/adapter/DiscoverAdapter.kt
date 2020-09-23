package ph.movieguide.com.features.discover.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ph.movieguide.com.R
import ph.movieguide.com.data.vo.MovieScreen
import ph.movieguide.com.di.providesBaseUrlImagePoster

class DiscoverAdapter(
    private val itemListener: OnItemClickListener
) : RecyclerView.Adapter<ViewHolderDiscover>() {

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderDiscover {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolderDiscover(inflater.inflate(R.layout.item_discover, parent, false))
    }

    override fun getItemCount(): Int = dataSources.size

    override fun onBindViewHolder(holder: ViewHolderDiscover, position: Int) {
        holder.bind(dataSources[position], itemListener, position, baseUrl)
    }
}