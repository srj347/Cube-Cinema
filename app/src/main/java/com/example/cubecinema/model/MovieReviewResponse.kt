package com.example.cubecinema.model

import com.google.gson.annotations.SerializedName

data class MovieReviewResponse(
    @field:SerializedName("results")
    val reviewsList: ArrayList<MovieReview>? = null
) {
}