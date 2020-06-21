package com.example.caiyunmvvmdemo.data

import com.google.gson.annotations.SerializedName

//实时天气
class RealtimeResponse(
    val status: String,
    val result: Result
) {

    data class Result(val realtime: Realtime)

    data class Realtime(
        val temperature: Float,
        val skyCon: String,
        @SerializedName("air_quality") val airQuality: AirQuality
    )

    data class AirQuality(val aqi: AQI)

    data class AQI(val chn: Float)

}