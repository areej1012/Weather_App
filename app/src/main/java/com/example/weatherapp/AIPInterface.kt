package com.example.weatherapp

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface AIPInterface {
    @GET("weather?")
    fun getWeather(@Query("q") city: String , @Query("appid") key : String, @Query("units") units:String ): Call<Weather>
}