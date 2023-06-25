package com.example.cubecinema.listeners

import com.google.android.material.bottomsheet.BottomSheetDialogFragment

interface BottomSheetListener {
    fun onActionSelected(action: String?, sortingBottomSheet: BottomSheetDialogFragment)
}