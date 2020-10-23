package com.example.caiyunmvvmdemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_test_intent.*

class TestIntentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_intent)
        val t = intent.getStringExtra(Intent.EXTRA_TEXT)
        text.text = if(t==null) "hello" else t
        val data = intent.data
        Log.d("Intent","""
            scheme ${data?.scheme}
            host ${data?.host}
            port ${data?.port}
            path ${data?.path}
            query ${data?.query}
        """.trimIndent())
    }
}