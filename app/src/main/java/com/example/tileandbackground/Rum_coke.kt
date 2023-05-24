package com.example.tileandbackground

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.widget.ImageView

class Rum_coke : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rum_coke)

        //get reference to Image
        val backToMain = findViewById(R.id.backPage) as ImageView
        //click Listener
        backToMain.setOnClickListener{
            //what should it do
            setContentView(R.layout.activity_main)
        }

    }
}
