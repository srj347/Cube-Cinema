package com.example.cubecinema.repository.local

import com.example.cubecinema.model.Movie

class LocalMovieRepository(private val movieDao: MovieDao) {
    suspend fun insertMovie(movie: Movie): Boolean {
        return try {
            movieDao.insert(movie)
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getMovieById(id: Int): Movie? {
        return movieDao.getMovieById(id)
    }

    suspend fun getMovies(): List<Movie> {
        return movieDao.getMovies()
    }
}
