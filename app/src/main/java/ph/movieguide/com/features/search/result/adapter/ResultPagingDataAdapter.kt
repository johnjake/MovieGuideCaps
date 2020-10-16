package ph.movieguide.com.features.search.result.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import ph.movieguide.android_baseplate_persistence.model.DBMoviesNowPlaying
import ph.movieguide.com.BuildConfig
import ph.movieguide.com.R

class ResultPagingDataAdapter :
    PagingDataAdapter<DBMoviesNowPlaying, ResultPagingDataAdapter.ResultMoviesViewHolder>(COMPARATOR) {
    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<DBMoviesNowPlaying>() {
            override fun areItemsTheSame(oldItem: DBMoviesNowPlaying, newItem: DBMoviesNowPlaying) =
                oldItem.title == newItem.title

            override fun areContentsTheSame(oldItem: DBMoviesNowPlaying, newItem: DBMoviesNowPlaying) =
                oldItem.title == newItem.title
        }
    }

    override fun onBindViewHolder(holder: ResultMoviesViewHolder, position: Int) {
        holder.bind(item = getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultMoviesViewHolder {
        return ResultMoviesViewHolder.getInstance(parent)
    }

    class ResultMoviesViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        companion object {
            fun getInstance(parent: ViewGroup): ResultMoviesViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val view = inflater.inflate(R.layout.item_search_view, parent, false)
                return ResultMoviesViewHolder(view)
            }
        }

        private var imgMovies: ImageView = view.findViewById(R.id.imgSearch)
        private var txtTitle: TextView = view.findViewById(R.id.txtSearchTitle)
        private val baseUrl: String = BuildConfig.BASE_URL_POSTER

        fun bind(item: DBMoviesNowPlaying?) {
            val path = baseUrl+item?.poster_path
            txtTitle.text = item?.title
            imgMovies.load(path)
        }
    }
}