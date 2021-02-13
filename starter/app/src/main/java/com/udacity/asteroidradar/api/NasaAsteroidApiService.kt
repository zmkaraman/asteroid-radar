package com.udacity.asteroidradar.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.PictureOfDay
import kotlinx.coroutines.Deferred
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


private const val BASE_URL = "https://api.nasa.gov/"
private const val API_KEY = "HSebuQzA1rQQab9cOkchf97bphzfErrhTT9l4giP"

//https://api.nasa.gov/neo/rest/v1/feed?start_date=2020-01-10&end_date=2020-01-17&api_key=HSebuQzA1rQQab9cOkchf97bphzfErrhTT9l4giP

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()


private val retrofit = Retrofit
    .Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()


interface NasaAsteroidApiService {

    @GET("neo/rest/v1/feed")
    suspend fun getAstreoids(@Query("start_date") startDate: String,
                             @Query("api_key") apiKey: String = API_KEY) : ResponseBody


    @GET("planetary/apod")
    suspend fun getPictureOfDay(@Query("api_key") apiKey: String = API_KEY): PictureOfDay


    //TODO MERVE burai düzenle
    @GET("neo/rest/v1/feed")
    suspend fun getAstreoidsTest(@Query("start_date") startDate: String,
                             @Query("api_key") apiKey: String = API_KEY) : Deferred<NetworkAsteroidContainer>

}

object NasaAsteroidApi {

    val retrofitService : NasaAsteroidApiService by lazy {
        retrofit.create(NasaAsteroidApiService::class.java)
    }
}