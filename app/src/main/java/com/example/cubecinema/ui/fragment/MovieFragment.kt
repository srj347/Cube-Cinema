package com.example.cubecinema.ui.fragment

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.registerReceiver
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.Fade
import androidx.transition.Slide
import com.example.cubecinema.R
import com.example.cubecinema.adapter.MovieAdapter
import com.example.cubecinema.databinding.FragmentMovieBinding
import com.example.cubecinema.listeners.BottomSheetListener
import com.example.cubecinema.listeners.FragmentCommunicator
import com.example.cubecinema.listeners.ItemClickListener
import com.example.cubecinema.listeners.NetworkChangeListener
import com.example.cubecinema.model.Movie
import com.example.cubecinema.model.MovieResponse
import com.example.cubecinema.model.NetworkResponse
import com.example.cubecinema.utils.FragmentUtils
import com.example.cubecinema.utils.NetworkChangeReceiver
import com.example.cubecinema.viewmodel.MovieViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar


class MovieFragment : Fragment(), ItemClickListener, BottomSheetListener, NetworkChangeListener {

    private lateinit var networkChangeReceiver: NetworkChangeReceiver
    private lateinit var mBinding: FragmentMovieBinding
    private var fragmentCommunicator: FragmentCommunicator? = null
    private val movieViewModel: MovieViewModel = MovieViewModel()
    private var movieAdapter: MovieAdapter? = null
    private var movieList: ArrayList<Movie>? = null
    private var isNetworkAvailable: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentMovieBinding.inflate(layoutInflater, container, false)

        fragmentCommunicator = requireActivity() as FragmentCommunicator

        // Registering broadcast receiver for network change
        networkChangeReceiver = NetworkChangeReceiver()
        networkChangeReceiver.setNetworkChangeListener(this)
        requireActivity().registerReceiver(
            networkChangeReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )

        requestMovieData(movieViewModel)
        setMenuItemClickListener()
        return mBinding.root
    }

    override fun onNetworkChanged(isConnected: Boolean) {
        isNetworkAvailable = isConnected
        if (!isConnected) {
            mBinding.noInternetIv.visibility = View.VISIBLE
        } else {
            // show fragment & get data from network
            mBinding.noInternetIv.visibility = View.GONE
            movieViewModel.getCurrentlyPlayingMovies()
        }
    }

    private fun setMenuItemClickListener() {
        mBinding.topAppBar.setOnMenuItemClickListener { menuItem ->
            if (!isNetworkAvailable) {
                Snackbar.make(mBinding.root, "No internet connection", Snackbar.LENGTH_SHORT).show()
            } else {
                when (menuItem.itemId) {
                    R.id.bookmark_item_menu -> {
                        FragmentUtils.addFragment(
                            requireActivity().supportFragmentManager,
                            MovieBookmarkFragment()
                        )
                        true
                    }

                    R.id.sort_item_menu -> {
                        // Show the bottom sheet
                        val sortingBottomSheet = SortingBottomSheet()
                        sortingBottomSheet.setBottomSheetListener(this)
                        FragmentUtils.showBottomSheet(
                            requireActivity().supportFragmentManager,
                            sortingBottomSheet
                        )
                        true
                    }
                }
            }
            false
        }
    }

    private fun requestMovieData(movieViewModel: MovieViewModel) {
        observeLiveDate(movieViewModel.movieList)
    }

    private fun <T> observeLiveDate(liveData: LiveData<NetworkResponse<T>>) {
        liveData.observe(viewLifecycleOwner) { response ->
            updateUI(response)
        }
    }

    private fun <T> updateUI(response: NetworkResponse<T>) {
        val message = when (response) {
            is NetworkResponse.Loading -> {
                mBinding.circularProgressBar.visibility = View.VISIBLE
                "Loading"
            }

            is NetworkResponse.Success -> {
                mBinding.circularProgressBar.visibility = View.GONE
                movieList = (response.data as MovieResponse).data as ArrayList<Movie>?
                setUpRecyclerView(movieList!!)
                for (data in movieList!!) {
                    Log.d(
                        "MovieDebugg",
                        "${data.id}, ${data.backdropPath}, ${data.length}, ${data.originalLanguage}, ${data.voteCount}"
                    )
                }
                "Success"
            }

            is NetworkResponse.Error -> {
                Snackbar.make(
                    mBinding.root,
                    "Movies not available, Please try again!",
                    Snackbar.LENGTH_SHORT
                ).show()
                "Error"
            }
        }
        Log.d("MovieDebugg", message)
    }

    private fun setUpRecyclerView(movieList: ArrayList<Movie>) {
        if (movieList.isEmpty()) {
            Snackbar.make(mBinding.root, "No movie found", Snackbar.LENGTH_SHORT).show()
        }
        movieAdapter = MovieAdapter(requireActivity(), movieList)
        movieAdapter!!.setOnItemClickListener(this)
        mBinding.moviesRv.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = movieAdapter
        }
    }

    override fun onItemClick(position: Int, itemId: Int) {
        if (itemId == -1) {
            val currentMovieId = movieViewModel.movieList.value?.data?.data?.get(position)?.id
            Log.d("MovieDebugg", "currentMovieId - ${currentMovieId}")
            fragmentCommunicator?.communicate(
                currentMovieId.toString(),
                this,
                MovieDetailFragment()
            )
        }
    }

    override fun onActionSelected(action: String?, sortingBottomSheet: BottomSheetDialogFragment) {
        if (action.equals("SortBy-ReleaseDate-ASC")) {
            val sortedList: ArrayList<Movie> = movieList?.toList() as ArrayList<Movie>
            sortMoviesByReleaseDate(sortedList)
            movieAdapter?.updateMovieList(sortedList)
            mBinding.moviesRv.scrollToPosition(0)

            // dismiss Bottom sheet
            FragmentUtils.dismissBottomSheet(sortingBottomSheet)
        }
    }

    private fun sortMoviesByReleaseDate(movieList: ArrayList<Movie>?) {
        movieList?.sort()
    }

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().unregisterReceiver(networkChangeReceiver)
    }

}