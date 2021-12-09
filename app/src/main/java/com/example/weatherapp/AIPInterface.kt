package com.example.weatherapp

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface AIPInterface {
    //http://api.openweathermap.org/data/2.5/weather?q=10001&appid=f1ec9853f58d17337e162700eac4fa3f
    @GET("weather?")
    fun getWeather(@Query("q") city: String , @Query("appid") key : String, @Query("units") units:String ): Call<Weather>
}