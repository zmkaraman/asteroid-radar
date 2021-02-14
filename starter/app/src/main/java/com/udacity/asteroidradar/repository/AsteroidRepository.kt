package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
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


    private val sdf = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT)
    private val currentDate = sdf.format(Date())


    val asteroids : LiveData<List<Asteroid>> = Transformations.map(database.asteroidDao.getAsteroids()) {
        it.asDomainModel()
    }

    val todaysAsteroids : LiveData<List<Asteroid>> = Transformations.map(database.asteroidDao.getAsteroidsByFilter(currentDate)) {
        it.asDomainModel()
    }

    suspend fun refreshAsteroids() {

        withContext(Dispatchers.IO) {

            var responseBody = NasaAsteroidApi.retrofitService.getAstreoids(currentDate)
            val asteroidList = NetworkAsteroidContainer(parseAsteroidsJsonResult(JSONObject(responseBody.string())))

            database.asteroidDao.insertAll(*asteroidList.asDatabaseModel())
        }
    }

    suspend fun deleteAll() {
        withContext(Dispatchers.IO) {

            val date = Calendar.getInstance()
            date.add(Calendar.DATE, -1)
            val yesterdaysDate = sdf.format(date)
            database.asteroidDao.deleteOldData(yesterdaysDate)
        }
    }

}