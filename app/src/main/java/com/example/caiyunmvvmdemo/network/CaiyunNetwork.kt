package com.example.caiyunmvvmdemo.network

import android.util.Log
import com.example.caiyunmvvmdemo.data.PlaceResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.RuntimeException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object CaiyunNetwork {
    val TAG="caiyunNetwork"
    private val placeService= ServiceCreator.create<PlaceService>()

    suspend fun searchPlaces(query: String) = placeService.serchPlaces(query).await()

    private suspend fun <T> Call<T>.await():T {
        return suspendCoroutine { continuation ->
            //进行网络请求，请求过程自动开子线程
            enqueue(object : Callback<T>{
                override fun onFailure(call: Call<T>, t: Throwable) {
                    Log.d(TAG,"onFailure")
                    t.printStackTrace()
                    continuation.resumeWithException(t)
                }

                override fun onResponse(call: Call<T>, response: Response<T>) {
                    Log.d(TAG,"onResponse")
                    val body= response.body()//得到的是结果，不是Call对象
                    if(body!=null) {
                        println("body $body")
                        /****************just println body as PlaceResponse ***************/
                        /*println((body as PlaceResponse).places)
                        for(item in (body as PlaceResponse).places)
                            println(item.toString())*/
                        /***********************************************/
                        continuation.resume(body)
                    }
                    else continuation.resumeWithException(
                        RuntimeException("response body is null")
                    )
                }

            })
        }
    }
}