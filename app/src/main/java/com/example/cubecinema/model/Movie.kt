package com.example.cubecinema.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat

@Entity(tableName = "movies")
data class Movie(
    @PrimaryKey
    @field:SerializedName("id")
    val id: Long? = null,

    @field:SerializedName("title")
    val title: String? = null,

    @field:SerializedName("backdrop_path")
    val backdropPath: String? = null,

    @field:SerializedName("poster_path")
    val posterPath : String? = null,

    @field:SerializedName("release_date")
    val releaseDate: String? = null,

    @field:SerializedName("overview")
    val overview: String? = null,

    @field:SerializedName("runtime")
    val length: Int? = null,

    @field:SerializedName("vote_count")
    val voteCount: Int? = null,

    @field:SerializedName("original_language")
    val originalLanguage: String? = null,

    @field:SerializedName("homepage")
    val homepageLink: String? = null
): Comparable<Movie> {

    override fun compareTo(other: Movie): Int {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val thisDate = dateFormat.parse(this.releaseDate)
        val otherDate = dateFormat.parse(other.releaseDate)
        return -thisDate.compareTo(otherDate)
    }
}
