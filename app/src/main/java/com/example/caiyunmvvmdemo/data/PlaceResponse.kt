package com.example.caiyunmvvmdemo.data

data class PlaceResponse(
    val status: String,
    val query: String,
    val places: List<Place>
)
