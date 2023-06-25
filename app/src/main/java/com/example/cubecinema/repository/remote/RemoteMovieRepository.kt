package com.example.cubecinema.repository.remote

import com.example.cubecinema.model.MovieCastResponse
import com.example.cubecinema.model.Movie
import com.example.cubecinema.model.MovieResponse
import com.example.cubecinema.model.MovieReviewResponse
import retrofit2.Response

class RemoteMovieRepository(
    private val movieApi: MovieApi
) {
    suspend fun getCurrentlyPlayingMovie(): Response<MovieResponse> {
        return movieApi.getCurrentlyPlayingMovies()
    }

    suspend fun getMovieById(movieId: Int): Response<Movie> {
        return movieApi.getMovieById(movieId)
    }

    suspend fun getCastForMovie(movieId: Int): Response<MovieCastResponse> {
        return movieApi.getCastsForMovie(movieId)
    }

    suspend fun getReviewForMovie(movieId: Int): Response<MovieReviewResponse> {
        return movieApi.getReviewForMovie(movieId)
    }
}