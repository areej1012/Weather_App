package com.example.weatherapp

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class APIClient {

    private var retrofit: Retrofit? = null

    fun getClient(): Retrofit?{
        retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            //convert the json to model
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit
    }



}