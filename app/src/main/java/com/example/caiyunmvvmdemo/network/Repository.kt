package com.example.caiyunmvvmdemo.network

import android.util.Log
import androidx.lifecycle.liveData
import com.example.caiyunmvvmdemo.data.Place
import com.example.caiyunmvvmdemo.data.Weather
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.lang.Exception
import java.lang.RuntimeException
import kotlin.coroutines.CoroutineContext

object Repository {

    //获取数据
    /*fun searchPlaces(query: String)= liveData(Dispatchers.IO) {
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
    }*/

    fun searchPlaces(query: String) =
        fire(Dispatchers.IO) {
            val placeResponse = CaiyunNetwork.searchPlaces(query)
            Log.d("rep","${placeResponse.status}")
            if (placeResponse.status == "ok") {
                val places = placeResponse.places
                Result.success(places)
            } else {
                Result.failure(RuntimeException("response status is ${placeResponse.status}"))
            }
        }


    fun refreshWeather(lng: String,lat: String) =
        fire(Dispatchers.IO){
            coroutineScope {
                val deferredRealtime = async {
                    CaiyunNetwork.getRealtimeResponse(lng,lat)
                }

                val deferredDaily = async {
                    CaiyunNetwork.getDailyResponse(lng,lat)
                }
                val realtimeResponse= deferredRealtime.await()

                val dailyResponse = deferredDaily.await()

                if(realtimeResponse.status=="ok" && dailyResponse.status=="ok"){
                    Result.success(Weather(
                        realtimeResponse.result.realtime,
                        dailyResponse.result.daily))
                }
                else{
                    Result.failure(
                        RuntimeException(
                            """
                                status1 is ${realtimeResponse.status}
                                status2 is ${dailyResponse.status}
                            """.trimIndent()
                        )
                    )
                }
            }
        }





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