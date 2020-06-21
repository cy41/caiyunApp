package com.example.caiyunmvvmdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.caiyunmvvmdemo.myFragment.PlaceFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction()
            .add(R.id.frameLayout,PlaceFragment())
            .commit()
    }
}