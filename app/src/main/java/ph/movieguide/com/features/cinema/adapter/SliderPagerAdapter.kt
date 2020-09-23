package ph.movieguide.com.features.cinema.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import coil.load
import ph.movieguide.com.BuildConfig
import ph.movieguide.com.R
import ph.movieguide.com.data.vo.MovieScreen

class SliderPagerAdapter(
    private val context: Context,
    private val listener: OnItemClickListener
) : PagerAdapter() {

    interface OnItemClickListener {
        fun onItemSliderClick(movieId: Int)
    }

    private var movieList: List<MovieScreen> = emptyList()

    var dataSource: List<MovieScreen>
        get() = movieList
        set(value) {
            movieList = value
            notifyDataSetChanged()
        }

    @SuppressLint("SetTextI18n")
    @NonNull
    override fun instantiateItem(@NonNull container: ViewGroup, position: Int): View {
        val baseUrlBackground = BuildConfig.BASE_URL_POSTER
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val slideLayout: View = inflater.inflate(R.layout.item_slider_visited, null)

        val slideImg = slideLayout.findViewById<ImageView>(R.id.imgSlider)

        val slideText = slideLayout.findViewById<TextView>(R.id.txtSliderTitle)

        val slideRating = slideLayout.findViewById<TextView>(R.id.sliderRating)

        val ratingBar: RatingBar = slideLayout.findViewById(R.id.sliderRatingBar)

        slideImg.load(baseUrlBackground + movieList[position].backdrop_path)

        val rating = movieList[position].vote_average
        val rate = rating/2
        ratingBar.rating = rate.toFloat()
        slideRating.text = "($rating)"
        slideText.text = movieList[position].title

        slideImg.setOnClickListener {
            listener.onItemSliderClick(movieList[position].id)
        }

        container.addView(slideLayout)
        return slideLayout
    }

    override fun getCount(): Int {
        return movieList.size
    }

    override fun isViewFromObject(@NonNull view: View, @NonNull o: Any): Boolean {
        return view === o
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        (container as ViewPager).removeView(`object` as View)
    }

}