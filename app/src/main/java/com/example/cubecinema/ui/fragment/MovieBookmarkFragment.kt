package com.example.cubecinema.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.cubecinema.R
import com.example.cubecinema.adapter.MovieAdapter
import com.example.cubecinema.adapter.MovieBookmarkAdapter
import com.example.cubecinema.databinding.FragmentMovieBookmarkBinding
import com.example.cubecinema.listeners.FragmentCommunicator
import com.example.cubecinema.listeners.ItemClickListener
import com.example.cubecinema.model.Movie
import com.example.cubecinema.utils.BookmarkManager

class MovieBookmarkFragment : Fragment(), ItemClickListener {

    private lateinit var mBinding: FragmentMovieBookmarkBinding
    private var bookmarkAdapter: MovieBookmarkAdapter? = null
    private var fragmentCommunicator: FragmentCommunicator? = null
    private lateinit var bookmarkManager: BookmarkManager

    override fun onStart() {
        super.onStart()
        mBinding.movieBookmarkToolbar.setNavigationOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentMovieBookmarkBinding.inflate(layoutInflater, container, false)

        fragmentCommunicator = requireActivity() as FragmentCommunicator
        bookmarkManager = BookmarkManager(requireActivity())
        setUpRecyclerView(bookmarkManager.getBookmarkedMovies())

        return mBinding.root
    }

    private fun setUpRecyclerView(movieList: List<Movie>) {
        if (movieList.isEmpty()) {
            showEmptyBookmarkLabel(true)
        } else {
            showEmptyBookmarkLabel(false)
            bookmarkAdapter = MovieBookmarkAdapter(requireActivity(), movieList as ArrayList<Movie>)
            bookmarkAdapter?.setOnItemClickListener(this)
            mBinding.movieBookmarkRv.apply {
                layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                adapter = bookmarkAdapter
            }
        }
    }

    private fun showEmptyBookmarkLabel(isMovieListEmpty: Boolean) {
        if (isMovieListEmpty) {
            mBinding.emptyBookmarkTv.visibility = View.VISIBLE
        } else {
            mBinding.emptyBookmarkTv.visibility = View.GONE
        }
    }

    override fun onItemClick(position: Int, itemId: Int) {
        if (itemId == R.id.movie_bookmark_remove_ib) {
            val movies = bookmarkManager.getBookmarkedMovies()
            bookmarkManager.unBookmarkMovie(movies.get(position))
            bookmarkAdapter?.removeMovie(position)
            if (bookmarkManager.getBookmarkedMovies().isEmpty()) {
                showEmptyBookmarkLabel(true)
            }
        } else {
            val currentMovieId = bookmarkManager.getBookmarkedMovies()[position].id
            fragmentCommunicator?.communicate(
                currentMovieId.toString(),
                this,
                MovieDetailFragment()
            )
        }
    }
}