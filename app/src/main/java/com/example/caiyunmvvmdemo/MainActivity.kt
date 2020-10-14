package com.example.caiyunmvvmdemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.caiyunmvvmdemo.myFragment.PlaceFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        handleIntent()
        supportFragmentManager.beginTransaction()
            .add(R.id.frameLayout,PlaceFragment())
            .commit()
    }

    private fun handleIntent(){
        val action = intent.action
        val type = intent.type
        if(action == Intent.ACTION_SEND_MULTIPLE && type == "image/*"){
            Toast.makeText(this, "hello world", Toast.LENGTH_SHORT).show()
        }
    }
}