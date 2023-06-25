package com.example.cubecinema.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cubecinema.databinding.FragmentSortingBottomSheetBinding
import com.example.cubecinema.listeners.BottomSheetListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SortingBottomSheet : BottomSheetDialogFragment() {
    private lateinit var mBinding: FragmentSortingBottomSheetBinding
    private var bottomSheetListener: BottomSheetListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentSortingBottomSheetBinding.inflate(inflater, container, false)
        attachOnClickListener()

        return mBinding.root
    }

    fun setBottomSheetListener(listener: BottomSheetListener) {
        this.bottomSheetListener = listener
    }

    private fun attachOnClickListener() {
        mBinding.itemSortByReleaseDateTv.setOnClickListener {
            bottomSheetListener?.onActionSelected("SortBy-ReleaseDate-ASC", this)
        }
        mBinding.itemSortByReleaseDateIb.setOnClickListener {
            bottomSheetListener?.onActionSelected("SortBy-ReleaseDate-ASC", this)
        }
    }
}