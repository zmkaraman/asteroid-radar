package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.api.NasaAsteroidApi
import com.udacity.asteroidradar.api.NetworkAsteroidContainer
import com.udacity.asteroidradar.api.asDatabaseModel
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AsteroidRepository(private val database: AsteroidDatabase) {


    val asteroids : LiveData<List<Asteroid>> = Transformations.map(database.asteroidDao.getAsteroids()) {
        it.asDomainModel()
    }

    suspend fun refreshVideos() {

        withContext(Dispatchers.IO) {
            //TODO MERVE bu tarih degisecek
            val asteroidList : NetworkAsteroidContainer = NasaAsteroidApi.retrofitService.getAstreoidsTest("2021-02-13").await()
            database.asteroidDao.insertAll(*asteroidList.asDatabaseModel())
        }
    }
}