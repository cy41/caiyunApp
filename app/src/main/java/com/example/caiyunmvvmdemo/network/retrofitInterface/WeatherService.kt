package com.example.caiyunmvvmdemo.network.retrofitInterface

import com.example.caiyunmvvmdemo.AppAplication
import com.example.caiyunmvvmdemo.data.DailyResponse
import com.example.caiyunmvvmdemo.data.RealtimeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

//Retrofit接口
interface WeatherService {

    @GET("v2.5/${AppAplication.TOKEN}/{lng},{lat}/realtime.json")
    fun getRealtimeResponse(
        @Path("lng") lng: String,
        @Path("lat") lat: String
    ): Call<RealtimeResponse>

    @GET("v2.5/${AppAplication.TOKEN}/{lng},{lat}/daily.json")
    fun getDailyResponse(
        @Path("lng") lng: String,
        @Path("lat") lat: String
    ): Call<DailyResponse>
}