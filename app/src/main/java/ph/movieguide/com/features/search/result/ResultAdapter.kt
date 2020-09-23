package ph.movieguide.com.features.search.result

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ph.movieguide.com.R
import ph.movieguide.com.data.vo.MovieScreen
import ph.movieguide.com.di.providesBaseUrlImagePoster

class ResultAdapter(
    private val itemListener: OnItemResultClickListener
) : RecyclerView.Adapter<ResultViewHolder>() {

    interface OnItemResultClickListener {
        fun onItemResultListener(movie: MovieScreen, position: Int)
    }

    private val baseUrl: String = providesBaseUrlImagePoster()

    private var dataSources: List<MovieScreen> = emptyList()

    var dataSource: List<MovieScreen>
        get() = dataSources
        set(value) {
            dataSources = value.take(10)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ResultViewHolder(inflater.inflate(R.layout.item_search_view, parent, false))
    }

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        holder.bind(dataSources[position], itemListener, position, baseUrl)
    }

    override fun getItemCount(): Int = dataSources.size
}