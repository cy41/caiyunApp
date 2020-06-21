package com.example.caiyunmvvmdemo.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.caiyunmvvmdemo.data.Place
import com.example.caiyunmvvmdemo.network.Repository

class PlaceViewModel: ViewModel() {
    private val searchLiveData= MutableLiveData<String>()

    val placeList= ArrayList<Place>()

    //当searchLiveData发生变化时，获取一个外部的LiveData对象转化为placeLiveData持有
    val placeLiveData = Transformations.switchMap(searchLiveData){ query ->
        Repository.searchPlaces(query)
    }

    fun searchPlaces(query: String){
        searchLiveData.value = query
        println("placeLiveData $placeLiveData")
    }
}