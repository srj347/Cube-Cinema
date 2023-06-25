package com.example.cubecinema.repository.remote

import com.example.cubecinema.di.NetworkModule
import com.example.cubecinema.model.MovieCastResponse
import com.example.cubecinema.model.Movie
import com.example.cubecinema.model.MovieResponse
import com.example.cubecinema.model.MovieReviewResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {

    @GET("now_playing")
    suspend fun getCurrentlyPlayingMovies(
        @Query("api_key") key: String = NetworkModule.API_KEY
    ): Response<MovieResponse>

    @GET("{movie_id}")
    suspend fun getMovieById(
        @Path("movie_id") movieId: Int,
        @Query("api_key") key: String = NetworkModule.API_KEY
    ): Response<Movie>

    @GET("{movie_id}/credits")
    suspend fun getCastsForMovie(
        @Path("movie_id") movieId: Int,
        @Query("api_key") key: String = NetworkModule.API_KEY
    ): Response<MovieCastResponse>

    @GET("{movie_id}/reviews")
    suspend fun getReviewForMovie(
        @Path("movie_id") movieId: Int,
        @Query("api_key") key: String = NetworkModule.API_KEY
    ): Response<MovieReviewResponse>

}