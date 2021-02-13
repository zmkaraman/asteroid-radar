package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.api.NasaAsteroidApi
import com.udacity.asteroidradar.api.NetworkAsteroidContainer
import com.udacity.asteroidradar.api.asDatabaseModel
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class AsteroidRepository(private val database: AsteroidDatabase) {


    val asteroids : LiveData<List<Asteroid>> = Transformations.map(database.asteroidDao.getAsteroids()) {
        it.asDomainModel()
    }

    suspend fun refreshAsteroids() {

        withContext(Dispatchers.IO) {
            //TODO MERVE  await ve Deferred olayını kaldırdım doğgru
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            val currentDate = sdf.format(Date())

            var responseBody = NasaAsteroidApi.retrofitService.getAstreoids(currentDate)
            val asteroidList = NetworkAsteroidContainer(parseAsteroidsJsonResult(JSONObject(responseBody.string())))

            database.asteroidDao.insertAll(*asteroidList.asDatabaseModel())
        }
    }
}