package com.example.caiyunmvvmdemo

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.contentValuesOf
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.caiyunmvvmdemo.adapter.WeatherAdapter
import com.example.caiyunmvvmdemo.data.ShowMltiData
import com.example.caiyunmvvmdemo.data.Weather
import com.example.caiyunmvvmdemo.data.getSky
import com.example.caiyunmvvmdemo.databinding.ActivityWeatherBinding
import com.example.caiyunmvvmdemo.viewModels.WeatherViewModel
import kotlinx.android.synthetic.main.now.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.jar.Manifest

class WeatherActivity : AppCompatActivity() {

    private val TAG = "WeatherActivity"
    private lateinit var binding: ActivityWeatherBinding
    private lateinit var timeChangeReceiver: BroadcastReceiver
    var hhh = 0
    val viewModel by lazy {
        ViewModelProviders.of(this).get(WeatherViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_weather)
        observeUI()
        initListener()

        createNotificationChannel()
        val intentFilter = IntentFilter()
        intentFilter.addAction("android.intent.action.TIME_TICK")
        timeChangeReceiver = TimeChangeReceiver()
        registerReceiver(timeChangeReceiver, intentFilter)

        binding.now.helloWorld.text = this.getString(R.string.hello_world, " ")
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            123 -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    call()
                else
                    Toast.makeText(this, "u should press allow", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun call(){
        val intent = Intent().apply {
            action = Intent.ACTION_CALL
            data = Uri.parse("tel:1008611")
        }
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(timeChangeReceiver)
    }

    fun initListener() {

        binding.swipeRefreshLayout.setOnRefreshListener {
            refreshWeather()
        }

        binding.now.share.setOnClickListener {
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "hello world")
                type = "text/plain"
            }
            startActivity(Intent.createChooser(intent, "test"))
        }

        binding.now.goweb.setOnClickListener {
            val intent = Intent(this, TestH5Activity::class.java)
            startActivity(intent)
        }
        binding.now.gogo.setOnClickListener {
            /*val uri = Uri.parse("http://www.cy41:41/hello")
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                addCategory("android.intent.category.DEFAULT")
                putExtra(Intent.EXTRA_TEXT, "test success")
                setDataAndType(uri, "text/plain")
            }
            if (intent.resolveActivity(packageManager) != null)
                startActivity(intent)*/
            /*val uri = Uri.parse("receiver://phone.start.receiver/test")
            val intent = Intent().apply {
                action = "com.example.caiyunmvvmdemo.phone_start_receiver"
                data = uri
                `package` = packageName
            }
            sendBroadcast(intent)*/

            val uri = Uri.parse("content://com.example.caiyunmvvmdemo")

            //find


            //insert
            val values = contentValuesOf("name" to "cy", "age" to "22")
            contentResolver.insert(uri, values)

            //upd
            val updValues = contentValuesOf("name" to "cy")
            contentResolver.update(uri, updValues, "name = ? and age = ?", arrayOf("cy", "22"))

            //del
            contentResolver.delete(uri, "name = ?", arrayOf("cy"))

        }

        binding.now.tel.setOnClickListener {
            if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CALL_PHONE),123)
            }
            else call()
        }


        binding.now.navBtn.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
        binding.drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerStateChanged(newState: Int) {}

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}

            override fun onDrawerClosed(drawerView: View) {
                val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                manager.hideSoftInputFromWindow(
                    drawerView.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
            }

            override fun onDrawerOpened(drawerView: View) {}

        })
    }

    fun refreshWeather() {
        viewModel.refreshWeather(viewModel.locationLng, viewModel.locationLat)
        binding.swipeRefreshLayout.isRefreshing = false
    }

    //设置viewModel中的一些参数，以及观察livedata数据变化
    fun observeUI() {
        viewModel.apply {
            if (locationLat.isEmpty()) {
                locationLat = intent.getStringExtra("location_lat") ?: ""
            }
            if (locationLng.isEmpty()) {
                locationLng = intent.getStringExtra("location_lng") ?: ""
            }
            if (placeName.isEmpty()) {
                placeName = intent.getStringExtra("place_name") ?: ""
            }
            Log.d("oui", "$locationLat $locationLng $placeName")

        }

        viewModel.weatherLiveData.observe(this, Observer { result ->
            val weather = result.getOrNull()
            if (weather != null) {
                Log.d(TAG, "success $weather")
                showWeather(weather)
            } else {
                Log.d(TAG, "failure")
                Toast.makeText(this, "can't catch things", Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.refreshWeather(viewModel.locationLng, viewModel.locationLat)
    }

    //显示指定的weather
    private fun showWeather(weather: Weather) {
        binding.now.placeName.text = viewModel.placeName
        val realtime = weather.realtime
        val daily = weather.daily
        binding.now.apply {
            currentTemp.text = "${realtime.temperature.toInt()} ℃"
            currentSky.text = getSky(realtime.skycon).info
            currentAQI.text = "空气指数 ${realtime.airQuality.aqi.chn.toInt()}"
        }

        hhh++
        val realtimeSky = getSky(realtime.skycon)
        binding.now.root.setBackgroundResource(realtimeSky.bg)
        val intent = Intent(this, ShowWeatherService::class.java).apply {
            putExtra("id",realtimeSky.icon)
        }
        startService(intent)

        val days = daily.skycon.size

        val list = arrayListOf<ShowMltiData>()
        if(hhh > list.size) hhh = 0
        for (i in 0 until days) {
            val skycon = daily.skycon[i]
            val temperature = daily.temperature[i]

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

        binding.fragmentForecast.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.fragmentForecast.recyclerView.adapter = WeatherAdapter(list)

        val lifeIndex = daily.lifeIndex
        binding.weatherLayout.visibility = View.VISIBLE
        binding.fragmentLifeIndex.apply {
            coldRiskText.text = lifeIndex.coldRisk[0].desc
            dressingText.text = lifeIndex.dressing[0].desc
            ultravioletText.text = lifeIndex.ultraviolet[0].desc
            carWashingText.text = lifeIndex.carWashing[0].desc
        }
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "caiyun_test"
            val descriptionText = "test"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("caiyunApp", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


    inner class TimeChangeReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            //createNotificationChannel()
            val intent = Intent(context, TestIntentActivity::class.java)
            val builder = NotificationCompat.Builder(context!!, "caiyunApp")
                .setSmallIcon(R.drawable.ic_heavy_rain) //状态栏图标，必须设
                .setContentTitle("test")
                .setContentText("测试内容")
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true) //单击推送后，推送消息消失
                .setPriority(NotificationCompat.PRIORITY_MAX) //设置通知优先级
                .setContentIntent(PendingIntent.getActivity(context, 0, intent, 0)) //单击推送消息时触发
            with(NotificationManagerCompat.from(context)) {
                notify(1, builder.build())
            }

        }

    }
}