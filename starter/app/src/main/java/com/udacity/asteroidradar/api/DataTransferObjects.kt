package com.udacity.asteroidradar.api

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import com.udacity.asteroidradar.database.DatabaseAsteroid


@JsonClass(generateAdapter = true)
data class NetworkAsteroidContainer(val videos: List<NetworkAsteroid>)



@JsonClass(generateAdapter = true)
data class NetworkAsteroid(val id: Long, val codename: String, val closeApproachDate: String,
                    val absoluteMagnitude: Double, val estimatedDiameter: Double,
                    val relativeVelocity: Double, val distanceFromEarth: Double,
                    val isPotentiallyHazardous: Boolean)


fun NetworkAsteroidContainer.asDatabaseModel(): Array<DatabaseAsteroid> {

    return videos.map {
        DatabaseAsteroid (
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }.toTypedArray()
}