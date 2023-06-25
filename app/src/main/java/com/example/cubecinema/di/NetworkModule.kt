package com.example.cubecinema.di

import com.example.cubecinema.repository.remote.MovieApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkModule {
    companion object {
        private val BASE_URL = "https://api.themoviedb.org/3/movie/"
        val API_KEY = "a1fee22bfdd6313d59d9812a28ff14bd"
        val POSTER_BASE_URL = "https://image.tmdb.org/t/p/original"

        private val loggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)

        private val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()


        fun getRetrofit(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        fun provideMovieService(retrofit: Retrofit): MovieApi {
            return retrofit.create(MovieApi::class.java)
        }
    }

}