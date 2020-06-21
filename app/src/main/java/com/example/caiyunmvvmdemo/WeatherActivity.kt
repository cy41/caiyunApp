package com.example.caiyunmvvmdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.caiyunmvvmdemo.data.Weather
import com.example.caiyunmvvmdemo.data.getSky
import com.example.caiyunmvvmdemo.databinding.ActivityWeatherBinding
import com.example.caiyunmvvmdemo.databinding.ForecastItemBinding
import com.example.caiyunmvvmdemo.viewModels.WeatherViewModel
import kotlinx.android.synthetic.main.forecast_item.*
import kotlinx.android.synthetic.main.fragment_forecast.*
import kotlinx.android.synthetic.main.fragment_life_index.*
import kotlinx.android.synthetic.main.fragment_weather.*
import kotlinx.android.synthetic.main.now.*
import java.text.SimpleDateFormat
import java.util.*

class WeatherActivity : AppCompatActivity() {

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
        }

        viewModel.weatherLiveData.observe(this, Observer { result ->
            val weather = result.getOrNull()
            if(weather!=null){
                showWeather(weather)
            }
            else{
                Toast.makeText(this,"can't catch things",Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun showWeather(weather: Weather){
        placeName.text=viewModel.placeName
        val realtime=weather.realtime
        val daily=weather.daily
        currentTemp.text="${realtime.temperature.toInt()} ℃"
        currentSky.text= getSky(realtime.skyCon).info

        currentAQI.text="空气指数 ${realtime.airQuality.aqi.chn.toInt()}"

        nowLayout.setBackgroundResource(getSky(realtime.skyCon).bg)

        forecastLayout.removeAllViews()

        val days=daily.skycon.size


        val itemBinding: ForecastItemBinding=DataBindingUtil.setContentView(this,R.layout.forecast_item)
        for(i in 0 until days){
            val skycon=daily.skycon[i]
            val temperature=daily.temperature[i]

            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            val sky = getSky(skycon.value)
            itemBinding.apply {
                dateInfo.text=simpleDateFormat.format(skycon.date)
                skyIcon.setImageResource(sky.icon)
                skyInfo.text=sky.info
                temperatureInfo.text="${temperature.min.toInt()} ~ ${temperature.max.toInt()} ℃"
            }

            forecastLayout.addView(itemBinding.root)
        }

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