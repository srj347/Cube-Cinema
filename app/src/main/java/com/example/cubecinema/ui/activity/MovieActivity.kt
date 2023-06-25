package com.example.cubecinema.ui.activity

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.cubecinema.ui.fragment.MovieFragment
import com.example.cubecinema.databinding.ActivityMovieBinding
import com.example.cubecinema.listeners.FragmentCommunicator
import com.example.cubecinema.ui.fragment.MovieBookmarkFragment
import com.example.cubecinema.ui.fragment.MovieDetailFragment
import com.example.cubecinema.utils.FragmentUtils
import com.example.cubecinema.utils.NetworkChangeReceiver

class MovieActivity : AppCompatActivity(), FragmentCommunicator {

    private lateinit var mBinding: ActivityMovieBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMovieBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        FragmentUtils.addFragment(supportFragmentManager, MovieFragment())
    }

    override fun communicate(data: String, fromFragment: Fragment, toFragment: Fragment) {
        when (toFragment) {
            is MovieBookmarkFragment -> {
                FragmentUtils.addFragment(supportFragmentManager, toFragment)
            }

            is MovieDetailFragment -> {
                val bundle = Bundle()
                bundle.putString("key_movie_id", data)
                toFragment.arguments = bundle

                if (fromFragment is MovieBookmarkFragment) {
                    FragmentUtils.replaceFragment(supportFragmentManager, toFragment)
                } else {
                    FragmentUtils.addFragment(supportFragmentManager, toFragment)
                }
            }
        }
    }

}