package ph.movieguide.com.features.search

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputLayout
import com.jakewharton.rxbinding3.widget.textChangeEvents
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.layout_search.*
import kotlinx.android.synthetic.main.layout_search.searchResultList as listResultView
import kotlinx.android.synthetic.main.toolbar_movie_details.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import ph.movieguide.com.R
import ph.movieguide.com.ext.hideKeyboard
import ph.movieguide.com.features.main.MainActivity
import ph.movieguide.com.features.search.result.MovieLoadStateAdapter
import java.util.concurrent.TimeUnit

class SearchFragment : Fragment() {

    private lateinit var resultLayout: LinearLayoutManager
    private val disposables = CompositeDisposable()
    private val clearDrawable by lazy {
        val width = requireContext().resources.getDimensionPixelOffset(R.dimen.include_clear_menu_width)
        val height = requireContext().resources.getDimensionPixelOffset(R.dimen.include_clear_menu_height)
        val textView = LayoutInflater.from(context).inflate(
            R.layout.include_clear_menu, null
        ) as TextView
        textView.layoutParams = LinearLayout.LayoutParams(width, height)

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        textView.run {
            layout(0, 0, width, height)
            draw(canvas)
        }
        BitmapDrawable(requireContext().resources, bitmap)
    }
    private val cursorStream = PublishSubject.create<Boolean>()
    private lateinit var searchLayout: TextInputLayout
    private lateinit var search: EditText
    private lateinit var loaderState: SearchLoaderStateAdapter
    private val searchAdapter: SearchPagingAdapter by lazy { SearchPagingAdapter() }
    private val viewModel: SearchPagingViewModel by inject<SearchPagingViewModel>()
    private var searchJob: Job? = null

    private fun implementSearchMovies(query: String) {
       searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.searchMovies(query).collectLatest { data ->
                searchAdapter.submitData(data)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_search, container, false)

    @ExperimentalPagingApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchLayout = view.findViewById(R.id.searchInputLayout)
        search = view.findViewById(R.id.search)

        val bottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNavigationView?.visibility = View.GONE

        val suggestLayout = LinearLayoutManager(requireContext()).apply {
            orientation = LinearLayoutManager.VERTICAL
        }

        searchLayout.apply {
            setEndIconOnClickListener {
                if (searchLayout.endIconDrawable != clearDrawable) {

                }
                if (search.text.toString().isEmpty()) {
                    search.hideKeyboard(requireActivity())
                    requireActivity().onBackPressed()
                } else {
                    search.setText("")
                }
            }
        }
        initPagingAdapter()
        implementSearchMovies(DEFAULT_QUERY_MOVIE)
    }

    override fun onStart() {
        super.onStart()
        toolbar_back.setOnClickListener {
            MainActivity.onBackPress = true
            this.findNavController().popBackStack()
        }
    }

    @ExperimentalPagingApi
    override fun onResume() {
        super.onResume()
        observeCursor()
        observeSearchView()
    }

    override fun onDestroyView() {
        disposables.clear()
        super.onDestroyView()
    }

    override fun onStop() {
        super.onStop()
        disposables.clear()
    }

    private fun initPagingAdapter() {
        resultLayout = LinearLayoutManager(requireContext()).apply {
            orientation = LinearLayoutManager.VERTICAL
        }

        val decoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)

        listResultView.apply {
            layoutManager = resultLayout
           adapter = searchAdapter.withLoadStateHeaderAndFooter(
               header = SearchLoaderStateAdapter { searchAdapter.retry() },
               footer = SearchLoaderStateAdapter { searchAdapter.retry() }
           )
            addItemDecoration(decoration)
        }

       searchAdapter.addLoadStateListener { loadState ->
            resultProgress.isVisible = loadState.source.refresh is LoadState.Loading
            // Toast on any error, regardless of whether it came from RemoteMediator or PagingSource
            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error
            errorState?.let {
                Toast.makeText(
                    context,
                    "\uD83D\uDE28 Whoops ${it.error}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        setupRecycleViewScrollListener()
    }


    @ExperimentalPagingApi
    @SuppressLint("DefaultLocale", "LogNotTimber")
    private fun observeSearchView() {
        // https://blog.mindorks.com/implement-search-using-rxjava-operators-c8882b64fe1d
        search.textChangeEvents()
            .doOnNext { event ->
                cursorStream.onNext(event.view.isFocused) }
            .doOnNext { event ->
                if (event.count == 0) {
                    searchLayout.setEndIconDrawable(R.drawable.ic_search_close)
                } else {
                    resultsGrp.visibility = View.VISIBLE
                    searchLayout.endIconDrawable = clearDrawable
                }
            }
            .map { event -> event.text }
            .map { text -> text.toString().toLowerCase().trim() }
            .debounce(250, TimeUnit.MILLISECONDS)
            .filter { text -> text.isNotBlank() }
            .distinctUntilChanged()
            .subscribe {
              implementSearchMovies(it)
            }
            .addTo(disposables)

           lifecycleScope.launch {
            searchAdapter.loadStateFlow
                .distinctUntilChangedBy { it.refresh }
                .filter { it.refresh is LoadState.Loading }
                .collectLatest { listResultView.scrollToPosition(0) }
            }
    }

    private fun observeCursor() {
        disposables.add(cursorStream.subscribe { showSoftKeyboard(it, search) })
    }

    private fun showSoftKeyboard(isFocused: Boolean, view: View) {
        if (isFocused) {
            (requireContext().getSystemService(
                Context.INPUT_METHOD_SERVICE) as InputMethodManager).apply {
                showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
            }
        } else {
            (requireContext().getSystemService(
                Context.INPUT_METHOD_SERVICE) as InputMethodManager).apply {
                hideSoftInputFromWindow(view.windowToken, InputMethodManager.SHOW_IMPLICIT)
            }
        }
    }

    private fun setupRecycleViewScrollListener() {
        listResultView.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalItemCount = resultLayout.itemCount
                val visibleItemCount = resultLayout.childCount
                val lastVisibleItem = resultLayout.findLastVisibleItemPosition()
            }
        })
    }

    companion object {
        const val DEFAULT_QUERY_MOVIE = "The Notebook"
    }
}