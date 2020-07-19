package com.example.messenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class FirstScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_screen)

        //최초 실행시 나타나는 화면
        //딜레이 1~2초 정도 주고 다음화면으로 바로 넘어갈 것
        val handler = Handler()
        handler.postDelayed({
            startActivity(Intent(this, LoadingWindow::class.java))
            finish()
        },2000)
    }
}