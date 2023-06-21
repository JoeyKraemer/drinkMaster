package com.example.ProjectDrinkMaster

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.ProjectDrinkMaster.MainActivity.Companion.newDrinkValueFile

class DrinksActivity: AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drinks)

        val resetDrinksButton = findViewById<Button>(R.id.resetDrinksButton)

        resetDrinksButton.setOnClickListener{
            newDrinkValueFile()
        }


    }
}