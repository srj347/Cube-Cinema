package com.example.cubecinema.model

import com.google.gson.annotations.SerializedName

data class MovieReview(
    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("content")
    val content: String? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("author_details")
    val authorDetails: AuthorDetail? = null
)