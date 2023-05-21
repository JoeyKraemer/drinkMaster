package com.example.tileandbackground

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //get reference to Image
        val arrow = findViewById(R.id.arrowGoToThirdPage) as ImageView
        //click Listener
        arrow.setOnClickListener{
            //what should it do
            setContentView(R.layout.activity_main)
        }
    }

}


