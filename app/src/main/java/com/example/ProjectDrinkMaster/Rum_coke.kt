package com.example.ProjectDrinkMaster

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class Rum_coke : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rum_coke)
        var backPage = findViewById(R.id.arrowChangePageLeft) as ImageView
        backPage.setOnClickListener{
            setContentView(R.layout.activity_main)
        }
    }
}
