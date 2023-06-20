package com.example.ProjectDrinkMaster

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent

class progressBar : AppCompatActivity() {

    private val splash_time: Long = 3000
    override  fun onCreate (savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ok_box)

        Handler().postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
         }, splash_time)

    }
}