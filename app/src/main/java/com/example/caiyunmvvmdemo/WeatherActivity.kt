package com.example.caiyunmvvmdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.caiyunmvvmdemo.adapter.WeatherAdapter
import com.example.caiyunmvvmdemo.data.ShowMltiData
import com.example.caiyunmvvmdemo.data.Weather
import com.example.caiyunmvvmdemo.data.getSky
import com.example.caiyunmvvmdemo.databinding.ActivityWeatherBinding
import com.example.caiyunmvvmdemo.viewModels.WeatherViewModel
import kotlinx.android.synthetic.main.fragment_forecast.view.*

import kotlinx.android.synthetic.main.fragment_life_index.*
import kotlinx.android.synthetic.main.now.*
import java.text.SimpleDateFormat
import java.util.*

class WeatherActivity : AppCompatActivity() {

    val TAG="WeatherActivity"
    lateinit var binding: ActivityWeatherBinding
    val viewModel by lazy {
        ViewModelProviders.of(this).get(WeatherViewModel::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_weather)
        //setContentView(R.layout.activity_weather)
        observeUI()


    }

    //设置viewModel中的一些参数，以及观察livedata数据变化
    fun observeUI(){
        viewModel.apply {
            if(locationLat.isEmpty()){
                locationLat=intent.getStringExtra("location_lat")?:""
            }
            if(locationLng.isEmpty()){
                locationLng=intent.getStringExtra("location_lng")?:""
            }
            if(placeName.isEmpty()){
                placeName=intent.getStringExtra("place_name")?:""
            }
            Log.d("oui","$locationLat $locationLng $placeName")

        }

        viewModel.weatherLiveData.observe(this, Observer { result ->
            val weather = result.getOrNull()
            if(weather!=null){
                Log.d(TAG,"success $weather")
                showWeather(weather)
            }
            else{
                Log.d(TAG,"failure")
                Toast.makeText(this,"can't catch things",Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.refreshWeather(viewModel.locationLng,viewModel.locationLat)
    }

    //显示指定的weather
    private fun showWeather(weather: Weather){
        placeName.text=viewModel.placeName
        val realtime=weather.realtime
        val daily=weather.daily
        currentTemp.text="${realtime.temperature.toInt()} ℃"
        currentSky.text= getSky(realtime.skycon).info

        currentAQI.text="空气指数 ${realtime.airQuality.aqi.chn.toInt()}"

        nowLayout.setBackgroundResource(getSky(realtime.skycon).bg)

        val days=daily.skycon.size

        val list= arrayListOf<ShowMltiData>()
        for(i in 0 until days){
            val skycon=daily.skycon[i]
            val temperature=daily.temperature[i]

            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            val sky = getSky(skycon.value)

            list.add(
                ShowMltiData(
                    simpleDateFormat.format(skycon.date),
                    sky.icon,
                    sky.info,
                    "${temperature.min.toInt()} ~ ${temperature.max.toInt()} ℃"
                )
            )
        }

        binding.fragmentForecast.recyclerView.layoutManager=LinearLayoutManager(this)
        binding.fragmentForecast.recyclerView.adapter=WeatherAdapter(list)


        val lifeIndex=daily.lifeIndex
        binding.apply {
            coldRiskText.text=lifeIndex.coldRisk[0].desc
            dressingText.text=lifeIndex.dressing[0].desc
            ultravioletText.text=lifeIndex.ultraviolet[0].desc
            carWashingText.text=lifeIndex.carWashing[0].desc
            weatherLayout.visibility= View.VISIBLE
        }
    }
}