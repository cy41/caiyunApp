package com.example.caiyunmvvmdemo.viewModels

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.caiyunmvvmdemo.data.Place
import com.example.caiyunmvvmdemo.Repository

class PlaceViewModel: ViewModel() {


    val num = MediatorLiveData<Int>()

    fun init() {
        num.addSource(searchLiveData){

            num.postValue(searchLiveData.value?.length)
        }
    }

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


    fun savePlace(place: Place) = Repository.savePlace(place)

    fun getSavedPlace() = Repository.getSavedPlace()

    fun isSavedPlace() = Repository.isPlaceSaved()
}