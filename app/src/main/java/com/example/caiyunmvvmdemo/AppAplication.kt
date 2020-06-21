package com.example.caiyunmvvmdemo

import android.app.Application
import android.content.Context

class AppAplication: Application() {
    companion object{
        const val TOKEN = ""
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context=applicationContext
    }
}