package com.example.cubecinema.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.cubecinema.R
import com.example.cubecinema.ui.fragment.MovieBookmarkFragment
import com.example.cubecinema.ui.fragment.MovieDetailFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

object FragmentUtils {
    fun addFragment(fragmentManager: FragmentManager, fragment: Fragment) {
        val transaction = fragmentManager.beginTransaction()

        // applying the transition
        when (fragment) {
            is MovieBookmarkFragment -> transaction.setCustomAnimations(
                R.anim.slide_up,
                R.anim.slide_down,
                R.anim.slide_up,
                R.anim.slide_down
            )

            is MovieDetailFragment -> transaction.setCustomAnimations(
                R.anim.slide_right,
                R.anim.slide_left,
                R.anim.fade_in,
                R.anim.fade_out
            )
        }

        if (fragmentManager.findFragmentById(R.id.movie_fragment_container) != null) {
            transaction.hide(fragmentManager.findFragmentById(R.id.movie_fragment_container)!!)
        }

        transaction
            .add(R.id.movie_fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    fun replaceFragment(fragmentManager: FragmentManager, fragment: Fragment) {
        val transaction = fragmentManager.beginTransaction()

        // applying the transition
        when (fragment) {
            is MovieBookmarkFragment -> transaction.setCustomAnimations(
                R.anim.slide_up,
                R.anim.slide_down,
                R.anim.slide_up,
                R.anim.slide_down
            )

            is MovieDetailFragment -> transaction.setCustomAnimations(
                R.anim.slide_right,
                R.anim.slide_left,
                R.anim.fade_in,
                R.anim.fade_out
            )
        }

        transaction
            .replace(R.id.movie_fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    fun showBottomSheet(fragmentManager: FragmentManager, fragement: BottomSheetDialogFragment) {
        fragement.show(fragmentManager, "SortingBottomSheet")
    }

    fun dismissBottomSheet(fragement: BottomSheetDialogFragment) {
        fragement.dismiss()
    }
}