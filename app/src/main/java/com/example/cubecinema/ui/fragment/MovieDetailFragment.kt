package com.example.cubecinema.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Fade
import androidx.transition.Slide
import com.bumptech.glide.Glide
import com.example.cubecinema.R
import com.example.cubecinema.adapter.MovieAdapter
import com.example.cubecinema.adapter.MovieCastAdapter
import com.example.cubecinema.adapter.MovieReviewAdapter
import com.example.cubecinema.adapter.utils.AdapterType
import com.example.cubecinema.databinding.FragmentMovieDetailBinding
import com.example.cubecinema.di.NetworkModule
import com.example.cubecinema.model.Movie
import com.example.cubecinema.model.MovieCast
import com.example.cubecinema.model.MovieCastResponse
import com.example.cubecinema.model.MovieReview
import com.example.cubecinema.model.MovieReviewResponse
import com.example.cubecinema.model.NetworkResponse
import com.example.cubecinema.ui.activity.MovieActivity
import com.example.cubecinema.utils.BookmarkManager
import com.example.cubecinema.utils.DateUtil
import com.example.cubecinema.viewmodel.MovieViewModel
import com.google.android.material.snackbar.Snackbar


class MovieDetailFragment : Fragment(), OnClickListener {

    private lateinit var mBinding: FragmentMovieDetailBinding
    private val movieViewModel: MovieViewModel = MovieViewModel()
    private var currentMovieId = -1
    private var currentMovie: Movie? = null
    private lateinit var bookmarkManager: BookmarkManager

    override fun onStart() {
        super.onStart()
        // setting the custom toolbar
        (activity as MovieActivity).setSupportActionBar(mBinding.movieDetailToolbar)
        mBinding.movieBookmarkIb.setOnClickListener(this)
        mBinding.watchMovieFab.setOnClickListener(this)
        mBinding.movieDetailToolbar.setNavigationOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentMovieDetailBinding.inflate(inflater, container, false)

        setEnterTransition(Slide(Gravity.RIGHT))

        bookmarkManager = BookmarkManager(requireActivity())
        currentMovieId = arguments?.getString("key_movie_id")!!.toInt()
        Log.d("MovieDebugg", "$currentMovieId")

        movieViewModel.getMovieById(currentMovieId)
        movieViewModel.getCastsForMovie(currentMovieId)
        movieViewModel.getReviewForMovie(currentMovieId)
        requestMovieData(movieViewModel)

        return mBinding.root
    }

    private fun requestMovieData(movieViewModel: MovieViewModel) {
        observeLiveDate(movieViewModel.movie)
        observeLiveDate(movieViewModel.castList)
        observeLiveDate(movieViewModel.reviewList)
    }

    private fun <T> observeLiveDate(liveData: LiveData<NetworkResponse<T>>) {
        liveData.observe(viewLifecycleOwner) { response ->
            updateUI(response)
        }
    }

    private fun <T> updateUI(response: NetworkResponse<T>) {
        val message = when (response) {
            is NetworkResponse.Loading -> "Loading..."
            is NetworkResponse.Success -> {
                val responseData = response.data
                if (responseData is MovieCastResponse) {
                    setUpRecyclerView(
                        responseData.movieCastList!!,
                        AdapterType.MOVIE_CAST_ADAPTER.value
                    )
                } else if (responseData is MovieReviewResponse) {
                    setUpRecyclerView(
                        responseData.reviewsList!!,
                        AdapterType.MOVIE_REVIEW_ADAPTER.value
                    )
                } else if (responseData is Movie) {
                    currentMovie = responseData as Movie
                    setUpMovieOverview(currentMovie!!)
                }
                "Success"
            }

            is NetworkResponse.Error -> {
                Snackbar.make(
                    mBinding.root,
                    "Movie not available, Please try again!",
                    Snackbar.LENGTH_SHORT
                ).show()
                "Error..."
            }
        }
        Log.d("MovieDebugg", message)
    }

    private fun setUpMovieOverview(movie: Movie) {
        mBinding.movieDetailToolbar.title = movie.title
        mBinding.movieDurationTv.text = "${currentMovie?.length.toString()} min"
        mBinding.movieReleaseTv.text =
            DateUtil.convertYYYYMMDDToReadableFormat(currentMovie?.releaseDate!!)
        mBinding.movieLangTv.text = "EN"
        mBinding.movieVotesTv.text = "${currentMovie?.voteCount.toString()} Votes"

        mBinding.movieOverviewTv.text = currentMovie?.overview
        Glide.with(requireActivity())
            .load(NetworkModule.POSTER_BASE_URL + currentMovie?.backdropPath)
            .into(mBinding.movieDetailPosterIv)
        if (bookmarkManager.isMovieBookmarked(movie)) {
            setBookmarkIcon(true)
        } else {
            setBookmarkIcon(false)
        }
    }

    private fun setUpRecyclerView(list: ArrayList<*>, adapterType: Int) {
        var recyclerAdapter: RecyclerView.Adapter<*>? = null
        var recyclerView: RecyclerView? = null
        when (adapterType) {
            AdapterType.MOVIE_ADAPTER.value -> {
                recyclerAdapter = MovieAdapter(requireActivity(), list as ArrayList<Movie>)
            }

            AdapterType.MOVIE_CAST_ADAPTER.value -> {
                recyclerAdapter = MovieCastAdapter(requireActivity(), list as ArrayList<MovieCast>)
                recyclerView = mBinding.movieCastRv
            }

            AdapterType.MOVIE_REVIEW_ADAPTER.value -> {
                recyclerAdapter =
                    MovieReviewAdapter(requireActivity(), list as ArrayList<MovieReview>)
                recyclerView = mBinding.movieReviewsRv
                if (list.size == 0) {
                    mBinding.noReviewsYetTv.visibility = View.VISIBLE
                }
            }
        }

        recyclerView?.apply {
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            adapter = recyclerAdapter
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.movie_bookmark_ib -> {
                currentMovie?.let { bookmarkMovie(it) }
            }

            R.id.watch_movie_fab -> {
                Log.d("MovieDebugg", currentMovie?.homepageLink.toString())
                openWebPageForWatchMovie()
            }
        }
    }

    private fun openWebPageForWatchMovie() {
        val movieWebpageLink = currentMovie?.homepageLink.toString()
        if (movieWebpageLink.isNullOrEmpty()) {
            Snackbar.make(mBinding.root, "Movie not available", Snackbar.LENGTH_SHORT).show()
            return
        }
        val uri = Uri.parse(movieWebpageLink)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    private fun bookmarkMovie(currentMovie: Movie) {
        if (bookmarkManager.isMovieBookmarked(currentMovie)) {
            // remove from bookmark
            bookmarkManager.unBookmarkMovie(currentMovie)
            setBookmarkIcon(false)
            Snackbar.make(mBinding.root, "Movie removed from Bookmark", Snackbar.LENGTH_SHORT)
                .show()
        } else {
            // Add to bookmark
            bookmarkManager.bookmarkMovie(currentMovie)
            setBookmarkIcon(true)
            Snackbar.make(mBinding.root, "Movie added to Bookmark", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun setBookmarkIcon(isAlreadBookmarked: Boolean) {
        if (isAlreadBookmarked) {
            mBinding.movieBookmarkIb.setBackgroundResource(R.drawable.ic_fav)
        } else {
            mBinding.movieBookmarkIb.setBackgroundResource(R.drawable.ic_fav_border)
        }
    }
}