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
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputLayout
import com.jakewharton.rxbinding3.widget.textChangeEvents
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.layout_search.*
import kotlinx.android.synthetic.main.layout_search.searchSuggestList as listView
import kotlinx.android.synthetic.main.layout_search.searchResultList as listResultView
import kotlinx.android.synthetic.main.toolbar_movie_details.*

import org.koin.androidx.viewmodel.ext.android.viewModel

import ph.movieguide.com.R

import ph.movieguide.com.data.vo.MovieScreen
import ph.movieguide.com.ext.hideKeyboard
import ph.movieguide.com.features.main.MainActivity
import ph.movieguide.com.features.search.result.ResultAdapter
import ph.movieguide.com.features.search.suggest.SuggestAdapter
import ph.movieguide.com.utils.State
import java.util.concurrent.TimeUnit

class SearchFragment : Fragment(),
    SuggestAdapter.OnItemClickListener,
    ResultAdapter.OnItemResultClickListener {

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
    private val viewModel: ViewModelSearch by viewModel()
    private val suggestAdapter by lazy { SuggestAdapter(this) }
    private val resultAdapter by lazy { ResultAdapter(this) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_search, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchLayout = view.findViewById(R.id.searchInputLayout)
        search = view.findViewById(R.id.search)

        val bottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNavigationView?.visibility = View.GONE

        val suggestLayout = LinearLayoutManager(requireContext()).apply {
            orientation = LinearLayoutManager.VERTICAL
        }

        val resultLayout = LinearLayoutManager(requireContext()).apply {
            orientation = LinearLayoutManager.VERTICAL
        }

        listView.apply {
            layoutManager = suggestLayout
            adapter = suggestAdapter
        }

        listResultView.apply {
            layoutManager = resultLayout
            adapter = resultAdapter
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

        suggestionObserver()
        searchObserver()
    }

    override fun onStart() {
        super.onStart()
        toolbar_back.setOnClickListener {
            MainActivity.onBackPress = true
            this.findNavController().popBackStack()
        }

        viewModel.getSearchSuggestionFromDB()
    }

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

    @SuppressLint("DefaultLocale", "LogNotTimber")
    private fun observeSearchView() {
        // https://blog.mindorks.com/implement-search-using-rxjava-operators-c8882b64fe1d
        search.textChangeEvents()
            .doOnNext { event ->
                cursorStream.onNext(event.view.isFocused) }
            .doOnNext { event ->
                if (event.count == 0) {
                    suggestionsGrp.visibility = View.VISIBLE
                    resultsGrp.visibility = View.GONE
                    searchLayout.setEndIconDrawable(R.drawable.ic_search_close)
                } else {
                    suggestionsGrp.visibility = View.GONE
                    resultsGrp.visibility = View.VISIBLE
                    searchLayout.endIconDrawable = clearDrawable
                }
            }
            .map { event -> event.text }
            .map { text -> text.toString().toLowerCase().trim() }
            .debounce(350, TimeUnit.MILLISECONDS)
            .filter { text -> text.isNotBlank() }
            .distinctUntilChanged()
            .subscribe {
                query -> viewModel.getSearchResult(query)
                /**  view model for search **/
            }
            .addTo(disposables)
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


    private fun suggestionObserver() {
        viewModel.stateLiveData.observe(viewLifecycleOwner, Observer { state -> handleScreenDBObserverState(state) })
    }

    private fun searchObserver() {
        viewModel.stateSearch.observe(viewLifecycleOwner, Observer { state -> handleSearchObserverState(state) })
    }

    /** observer state database **/
    private fun handleSearchObserverState(state: State<List<MovieScreen>>) {
        when (state) {
            is State.Data -> handleSearchSuccess(state.data)
            is State.Error -> handleSearchFailed(state.error)
        }
    }

    private fun handleSearchFailed(error: Throwable) {
        Toast.makeText(context, "Error On: ${error.message}", Toast.LENGTH_SHORT).show()
    }

    private fun handleSearchSuccess(data: List<MovieScreen>) {
        resultAdapter.dataSource = data
        viewModel.saveDiscoverList(data)
    }

    /** observer state database **/
    private fun handleScreenDBObserverState(state: State<List<MovieScreen>>) {
        when (state) {
            is State.Data -> handleScreenDBSuccess(state.data)
            is State.Error -> handleScreenDBFailed(state.error)
        }
    }

    private fun handleScreenDBFailed(error: Throwable) {
        Toast.makeText(context, "Error On: ${error.message}", Toast.LENGTH_SHORT).show()
    }

    private fun handleScreenDBSuccess(data: List<MovieScreen>) {
        suggestAdapter.dataSource = data
    }

    override fun onItemClick(movie: MovieScreen, position: Int) {
        MainActivity.onTopDetails = false
        MainActivity.onNowShowingDetails = true
        val actionByParam = SearchFragmentDirections.actionSearchFragmentToDetailsFragment(movie.id)
        findNavController().navigate(actionByParam)
    }

    override fun onItemResultListener(movie: MovieScreen, position: Int) {
        MainActivity.onTopDetails = false
        MainActivity.onNowShowingDetails = true
        val actionByParam = SearchFragmentDirections.actionSearchFragmentToDetailsFragment(movie.id)
        findNavController().navigate(actionByParam)
    }
}