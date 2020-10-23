package com.example.caiyunmvvmdemo

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat

class ShowWeatherService : Service() {

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onCreate() {
        super.onCreate()
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("myService", "下拉框显示", NotificationManager.IMPORTANCE_DEFAULT)
            manager.createNotificationChannel(channel)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val sendIntent = Intent(this, MainActivity::class.java)
        val pi = PendingIntent.getActivity(this, 0, sendIntent, 0)
        val notification = NotificationCompat.Builder(this, "myService")
            .setContentTitle("hello")
            .setContentText("hello world")
            .setSmallIcon(intent!!.getIntExtra("id", 0))
            .setContentIntent(pi)
            .build()
        startForeground(2, notification)
        return super.onStartCommand(intent, flags, startId)
    }
}
