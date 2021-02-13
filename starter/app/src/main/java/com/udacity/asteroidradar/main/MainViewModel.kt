package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.NasaAsteroidApi
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch


enum class NasaApiStatus { LOADING, ERROR, DONE }

class MainViewModel(application: Application) : AndroidViewModel(application) {


    private val database = getDatabase(application)
    private val asteroidRepository = AsteroidRepository(database)


    private val _status = MutableLiveData<NasaApiStatus>()
    val status: LiveData<NasaApiStatus>
        get() = _status

    private val _potd = MutableLiveData<PictureOfDay>()

    val potd: LiveData<PictureOfDay>
        get() = _potd

    private val _navigateToSelectedAsteroid = MutableLiveData<Asteroid>()

    val navigateToSelectedAsteroid: LiveData<Asteroid>
        get() = _navigateToSelectedAsteroid

    init {
        getPictureOfDay()

        viewModelScope.launch {
            asteroidRepository.refreshAsteroids()
        }
    }

    val asteroids = asteroidRepository.asteroids


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

    fun displayAsteroidDetails(asteroid: Asteroid) {
        _navigateToSelectedAsteroid.value = asteroid
    }

    fun displayAsteroidDetailsComplete() {
        _navigateToSelectedAsteroid.value = null
    }

    /**
     * Factory for constructing MainViewModel with parameter
     */
    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}