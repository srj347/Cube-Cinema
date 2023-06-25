package com.example.cubecinema.listeners

import androidx.fragment.app.Fragment

interface FragmentCommunicator {
    fun communicate(data: String, fromFragment: Fragment, toFragment: Fragment)
}