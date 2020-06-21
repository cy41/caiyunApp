package com.example.caiyunmvvmdemo.data

data class Weather(
    val realtime: RealtimeResponse.Realtime,
    val daily: DailyResponse.Daily
)