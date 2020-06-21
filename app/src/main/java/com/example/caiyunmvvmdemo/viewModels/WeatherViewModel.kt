package com.example.caiyunmvvmdemo.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.caiyunmvvmdemo.data.Location
import com.example.caiyunmvvmdemo.network.Repository
import kotlinx.coroutines.runBlocking

class WeatherViewModel: ViewModel() {
    private val locationLiveData = MutableLiveData<Location>()

    var locationLng = ""
    var locationLat = ""
    var placeName = ""

    val weatherLiveData = Transformations.switchMap(locationLiveData){location ->
        Repository.refreshWeather(location.lng,location.lat)
    }

    fun refreshWeather(lng: String,lat: String){
        locationLiveData.value= Location(lng,lat)
    }

}