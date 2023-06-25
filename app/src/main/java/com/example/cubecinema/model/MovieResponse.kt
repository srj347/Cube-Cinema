package com.example.cubecinema.model

import com.google.gson.annotations.SerializedName

data class MovieResponse(
	@field:SerializedName("results")
	val data: List<Movie?>? = null
)
