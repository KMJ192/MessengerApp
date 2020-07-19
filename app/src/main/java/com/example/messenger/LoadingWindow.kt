package com.example.messenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class LoadingWindow : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading_window)

        val handler = Handler()
        handler.postDelayed({
            startActivity(Intent(this@LoadingWindow, LoginActivity::class.java))
            finish()
        },3500)
    }
}