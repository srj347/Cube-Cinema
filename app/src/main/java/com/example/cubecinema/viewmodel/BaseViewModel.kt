package com.example.cubecinema.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cubecinema.model.NetworkResponse
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception

open class BaseViewModel : ViewModel() {
    fun <T> performNetworkRequest(
        request: suspend () -> Response<T>,
        responseLiveData: MutableLiveData<NetworkResponse<T>>
    ) {
        responseLiveData.value = NetworkResponse.Loading()
        viewModelScope.launch {
            try {
                val response = request.invoke()
                if (response.isSuccessful) {
                    responseLiveData.value = NetworkResponse.Success(response.body()!!)
                } else {
                    responseLiveData.value =
                        NetworkResponse.Error("Unable to load the data from remote")
                }
            } catch (e: Exception) {
                responseLiveData.value = NetworkResponse.Error("Exception occurred: ${e.message}")
                e.printStackTrace()
            }
        }
    }
}