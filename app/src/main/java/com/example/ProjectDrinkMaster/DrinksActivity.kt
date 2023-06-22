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
            //this makes the app restart automatically
            val ctx = applicationContext
            val pm = ctx.packageManager
            val intent = pm.getLaunchIntentForPackage(ctx.packageName)
            val mainIntent = Intent.makeRestartActivityTask(intent!!.component)
            ctx.startActivity(mainIntent)
            Runtime.getRuntime().exit(0)
        }
        defaultValuesButton.setOnClickListener{
            resetDrinksToDefaultValues()
            val ctx = applicationContext
            val pm = ctx.packageManager
            val intent = pm.getLaunchIntentForPackage(ctx.packageName)
            val mainIntent = Intent.makeRestartActivityTask(intent!!.component)
            ctx.startActivity(mainIntent)
            Runtime.getRuntime().exit(0)
        }
        accessDrinksButton.setOnClickListener{
            val intent = Intent(this@DrinksActivity, EditDrinksActivity::class.java)
            startActivity(intent);
        }


    }
}