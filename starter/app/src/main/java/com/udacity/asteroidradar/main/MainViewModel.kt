package com.udacity.asteroidradar.main

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.api.NasaAsteroidApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val _asteroids = MutableLiveData<List<Asteroid>>()

    val asteroids: LiveData<List<Asteroid>>
        get() = _asteroids


    init {
        getAsteroids() //TODO MERVE belki filtreli olur
    }

    private fun getAsteroids() {

        viewModelScope.launch {

            //_status.value = MarsApiStatus.LOADING
            //TODO MERVE LOADINGLERI DUZELT
            try {

                //TODO MERVE dateler sysdate olacak
                val responseBody = NasaAsteroidApi.retrofitService.getAstreoids(
                    "2021-02-12")

                _asteroids.value = parseAsteroidsJsonResult(
                    JSONObject(responseBody.string())
                )

                var size = asteroids.value?.size

                //_status.value = MarsApiStatus.DONE
            } catch (e: Exception) {
                // _status.value = MarsApiStatus.ERROR
                _asteroids.value = ArrayList()
            }

        }
    }
}