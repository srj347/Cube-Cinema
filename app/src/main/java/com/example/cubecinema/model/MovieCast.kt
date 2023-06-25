package com.example.cubecinema.model

import com.google.gson.annotations.SerializedName

data class MovieCast(
    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("profile_path")
    val profilePath: String? = null,

    @field:SerializedName("known_for_department")
    val department: String? = null,

    @field:SerializedName("popularity")
    val popularity: Double? = null
) {
}