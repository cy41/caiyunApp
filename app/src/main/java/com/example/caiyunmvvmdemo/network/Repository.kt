package com.example.caiyunmvvmdemo.network

import android.util.Log
import androidx.lifecycle.liveData
import com.example.caiyunmvvmdemo.data.Place
import kotlinx.coroutines.Dispatchers
import java.lang.Exception
import java.lang.RuntimeException
import kotlin.coroutines.CoroutineContext

object Repository {

    //获取数据
    fun searchPlaces(query: String)= liveData(Dispatchers.IO) {
        val result= try {
            val placeResponse= CaiyunNetwork.searchPlaces(query)
            if(placeResponse.status=="ok"){
                val places=placeResponse.places
                Result.success(places)
            }
            else{
                Result.failure(RuntimeException("response status is ${placeResponse.status}"))
            }
        }catch (e: Exception){
            Result.failure<List<Place>>(e)
        }

        emit(result as Result<List<Place>>)
    }

   /* fun searchPlaces(query: String) =
        fire(Dispatchers.IO) {

            val placeResponse =
                CaiyunNetwork.searchPlaces(
                    query
                )
            Log.d("rep","${placeResponse.status}")
            if (placeResponse.status == "ok") {
                val places = placeResponse.places
                Result.success(places)
            } else {
                Result.failure(RuntimeException("response status is ${placeResponse.status}"))
            }
        }*/

    private fun <T> fire(context: CoroutineContext, block: suspend () -> Result<T>) =
        liveData<Result<T>>(context) {
            val result = try {
                block()
            } catch (e: Exception) {
                Result.failure<T>(e)
            }
            emit(result)
        }
}