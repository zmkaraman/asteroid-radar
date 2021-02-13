package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.NasaAsteroidApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import kotlinx.coroutines.launch
import org.json.JSONObject


enum class NasaApiStatus { LOADING, ERROR, DONE }

class MainViewModel : ViewModel() {

    private val _status = MutableLiveData<NasaApiStatus>()

    val status: LiveData<NasaApiStatus>
        get() = _status

    private val _asteroids = MutableLiveData<List<Asteroid>>()

    val asteroids: LiveData<List<Asteroid>>
        get() = _asteroids

    private val _potd = MutableLiveData<PictureOfDay>()

    val potd: LiveData<PictureOfDay>
        get() = _potd

    private val _navigateToSelectedAsteroid = MutableLiveData<Asteroid>()

    val navigateToSelectedAsteroid: LiveData<Asteroid>
        get() = _navigateToSelectedAsteroid

    init {
        getAsteroids() //TODO MERVE belki filtreli olur
        getPictureOfDay()
    }

    private fun getPictureOfDay() {


        viewModelScope.launch {

            _status.value = NasaApiStatus.LOADING
            try {

                val responseBody = NasaAsteroidApi.retrofitService.getPictureOfDay()
                _potd.value = responseBody
                _status.value = NasaApiStatus.DONE

            } catch (e: Exception) {
                _status.value = NasaApiStatus.ERROR
                _potd.value = null
            }

        }
    }

    private fun getAsteroids() {

        viewModelScope.launch {

            _status.value = NasaApiStatus.LOADING
            try {

                //TODO MERVE dateler sysdate olacak
                val responseBody = NasaAsteroidApi.retrofitService.getAstreoids(
                    "2021-02-12")

                _asteroids.value = parseAsteroidsJsonResult(
                    JSONObject(responseBody.string())
                )

                _status.value = NasaApiStatus.DONE

            } catch (e: Exception) {
                _status.value = NasaApiStatus.ERROR
                _asteroids.value = ArrayList()
            }

        }
    }

    fun displayAsteroidDetails(asteroid: Asteroid) {
        _navigateToSelectedAsteroid.value = asteroid
    }

    fun displayAsteroidDetailsComplete() {
        _navigateToSelectedAsteroid.value = null
    }
}