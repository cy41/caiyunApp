package com.example.caiyunmvvmdemo.myReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class PhoneStartReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Toast.makeText(context, "hello world", Toast.LENGTH_SHORT).show()
        /*intent.data?.apply {
            var str = ""
            str += scheme
            str += "://"
            str += host
            str += "/"
            str += path
            Toast.makeText(context, str, Toast.LENGTH_SHORT).show()
        }*/
    }
}
