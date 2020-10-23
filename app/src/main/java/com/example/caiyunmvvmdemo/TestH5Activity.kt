package com.example.caiyunmvvmdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_test_h5.*

class TestH5Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_h5)
        //web_view.loadUrl("https://www.baidu.com")
        web_view.loadUrl("file:///android_asset/helloworld.html")
    }
}