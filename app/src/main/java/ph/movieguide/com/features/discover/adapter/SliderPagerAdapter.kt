package ph.movieguide.com.features.discover.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import coil.load
import ph.movieguide.com.BuildConfig
import ph.movieguide.com.R
import ph.movieguide.com.data.vo.MovieScreen


class SliderPagerAdapter(
    private val context: Context
) : PagerAdapter() {

    private var movieList: List<MovieScreen> = emptyList()

    var dataSource: List<MovieScreen>
        get() = movieList
        set(value) {
            movieList = value
        }

    @SuppressLint("SetTextI18n")
    @NonNull
    override fun instantiateItem(@NonNull container: ViewGroup, position: Int): View {
        val baseUrlBackground = BuildConfig.BASE_URL_ORIGINAL
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val slideLayout: View = inflater.inflate(R.layout.item_slider_discover, null)
        val slideImg = slideLayout.findViewById<ImageView>(R.id.slide_img)
        val slideText = slideLayout.findViewById<TextView>(R.id.slide_title)
        val slideRelease = slideLayout.findViewById<TextView>(R.id.slide_release)
        slideImg.load(baseUrlBackground + movieList[position].backdrop_path)
        val dates = movieList[position].release_date
        slideRelease.text = "Theater: $dates"
        slideText.text = movieList[position].title
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