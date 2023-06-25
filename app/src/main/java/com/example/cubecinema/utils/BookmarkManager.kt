package com.example.cubecinema.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.cubecinema.model.Movie
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class BookmarkManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("BookmarkPrefs", Context.MODE_PRIVATE)
    private val gson: Gson = Gson()

    fun bookmarkMovie(movie: Movie) {
        val bookmarkedMovies = getBookmarkedMovies().toMutableList()
        if (!bookmarkedMovies.contains(movie)) {
            bookmarkedMovies.add(movie)
            saveBookmarkedMovies(bookmarkedMovies)
        }
    }

    fun unBookmarkMovie(movie: Movie) {
        val bookmarkedMovies = getBookmarkedMovies().toMutableList()
        if (bookmarkedMovies.contains(movie)) {
            bookmarkedMovies.remove(movie)
            saveBookmarkedMovies(bookmarkedMovies)
        }
    }

    fun isMovieBookmarked(movie: Movie): Boolean {
        val bookmarkedMovies = getBookmarkedMovies()
        return bookmarkedMovies.contains(movie)
    }

    fun getBookmarkedMovies(): List<Movie> {
        val bookmarkedMoviesJson = sharedPreferences.getString("bookmarkedMovies", "")
        val type = object : TypeToken<List<Movie>>() {}.type
        return gson.fromJson(bookmarkedMoviesJson, type) ?: emptyList()
    }

    private fun saveBookmarkedMovies(movies: List<Movie>) {
        val bookmarkedMoviesJson = gson.toJson(movies)
        sharedPreferences.edit().putString("bookmarkedMovies", bookmarkedMoviesJson).apply()
    }
}
