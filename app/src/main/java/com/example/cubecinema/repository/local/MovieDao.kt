package com.example.cubecinema.repository.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.cubecinema.model.Movie

@Dao
interface MovieDao {
    @Insert
    suspend fun insert(movie: Movie)

    @Query("SELECT * FROM movies WHERE id = :id")
    suspend fun getMovieById(id: Int): Movie?

    @Query("SELECT * FROM movies")
    suspend fun getMovies(): List<Movie>
}
