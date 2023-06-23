package com.example.ProjectDrinkMaster

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent

class ProgressBar : AppCompatActivity() {
    private lateinit var mainActivity: MainActivity

    public val splash_time: Long = mainActivity.getTime()
    override  fun onCreate (savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ok_box)

        Handler().postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
         }, splash_time)

    }

    public fun getTime(): Long {
        return splash_time
    }
}
