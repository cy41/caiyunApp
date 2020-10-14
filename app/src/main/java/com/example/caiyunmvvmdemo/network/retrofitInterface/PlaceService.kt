package com.example.caiyunmvvmdemo.network.retrofitInterface

import com.example.caiyunmvvmdemo.AppAplication
import com.example.caiyunmvvmdemo.data.PlaceResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

//Retrofit接口
interface PlaceService {

    @GET("v2/place?token=${AppAplication.TOKEN}&lang=zh_CN")
    fun searchPlaces(@Query("query") query: String): Call<PlaceResponse>
}