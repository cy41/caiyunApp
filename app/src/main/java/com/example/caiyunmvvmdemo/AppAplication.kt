package com.example.caiyunmvvmdemo

import android.app.Application
import android.content.Context

class AppAplication: Application() {
    companion object{
        const val TOKEN = "c6VOYL5xHedPRm6l"
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context=applicationContext
    }
}