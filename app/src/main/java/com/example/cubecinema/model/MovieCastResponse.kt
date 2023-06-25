package com.example.cubecinema.model

import com.google.gson.annotations.SerializedName

data class MovieCastResponse(
    @field:SerializedName("cast")
    val movieCastList: ArrayList<MovieCast>? = null
) {
}