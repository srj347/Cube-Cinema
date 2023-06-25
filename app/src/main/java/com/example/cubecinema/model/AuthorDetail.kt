package com.example.cubecinema.model

import com.google.gson.annotations.SerializedName

data class AuthorDetail(
    @field:SerializedName("username")
    val author: String? = null,

    @field:SerializedName("rating")
    val rating: Int? = null,

    @field:SerializedName("avatar_path")
    val profilePath: String? = null
)
