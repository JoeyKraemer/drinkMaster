package com.example.ProjectDrinkMaster

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.ProjectDrinkMaster.MainActivity.Companion.newDrinkValueFile
import com.example.ProjectDrinkMaster.MainActivity.Companion.resetDrinksToDefaultValues

class DrinksActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drinks)

        val resetDrinksButton = findViewById<Button>(R.id.resetDrinksButton)
        val defaultValuesButton = findViewById<Button>(R.id.defaultValuesButton)
        val accessDrinksButton = findViewById<Button>(R.id.accessEditableDrinksButton)

        resetDrinksButton.setOnClickListener{
            newDrinkValueFile()
        }
        defaultValuesButton.setOnClickListener{
            resetDrinksToDefaultValues()
        }
        accessDrinksButton.setOnClickListener{
            val intent = Intent(this@DrinksActivity, EditDrinksActivity::class.java)
            startActivity(intent);
        }


    }
}