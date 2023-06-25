package com.example.cubecinema.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.cubecinema.di.LocalDBModule
import com.example.cubecinema.di.NetworkModule
import com.example.cubecinema.model.MovieCastResponse
import com.example.cubecinema.model.Movie
import com.example.cubecinema.model.MovieResponse
import com.example.cubecinema.model.MovieReviewResponse
import com.example.cubecinema.model.NetworkResponse
import com.example.cubecinema.repository.local.LocalMovieRepository
import com.example.cubecinema.repository.remote.MovieApi
import com.example.cubecinema.repository.remote.RemoteMovieRepository

class MovieViewModel(
    val remoteMovieRepository: RemoteMovieRepository = RemoteMovieRepository(
        NetworkModule.provideMovieService(
            NetworkModule.getRetrofit()
        )
    ),
) : BaseViewModel() {

    var movieList: MutableLiveData<NetworkResponse<MovieResponse>> = MutableLiveData()
    var movie: MutableLiveData<NetworkResponse<Movie>> = MutableLiveData()
    var castList: MutableLiveData<NetworkResponse<MovieCastResponse>> = MutableLiveData()
    var reviewList: MutableLiveData<NetworkResponse<MovieReviewResponse>> = MutableLiveData()

    fun getCurrentlyPlayingMovies() {
        performNetworkRequest(
            request = { remoteMovieRepository.getCurrentlyPlayingMovie() },
            responseLiveData = movieList
        )
    }

    fun getMovieById(movieId: Int) {
        performNetworkRequest(
            request = { remoteMovieRepository.getMovieById(movieId) },
            responseLiveData = movie
        )
    }

    fun getCastsForMovie(movieId: Int) {
        performNetworkRequest(
            request = { remoteMovieRepository.getCastForMovie(movieId) },
            responseLiveData = castList
        )
    }

    fun getReviewForMovie(movieId: Int) {
        performNetworkRequest(
            request = { remoteMovieRepository.getReviewForMovie(movieId) },
            responseLiveData = reviewList
        )
    }

}
