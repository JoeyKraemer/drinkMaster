package com.example.ProjectDrinkMaster

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.ProjectDrinkMaster.MainActivity.Companion.newDrinkValueFile
import com.example.ProjectDrinkMaster.MainActivity.Companion.resetDrinksToDefaultValues

// access drinks page
class DrinksActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drinks)

        // == get buttons ==
        val resetDrinksButton = findViewById<ImageView>(R.id.resetDrink)
        val defaultValuesButton = findViewById<ImageView>(R.id.defaultDrink)
        val accessDrinksButton = findViewById<ImageView>(R.id.accesDrink)
        val returnButton = findViewById<ImageView>(R.id.returnBack)

        // == get onclick listeners for buttons ==
        // completely rewrites drinkValues.json file
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
        // changes drinkValues.json file back to default values
        defaultValuesButton.setOnClickListener{
            resetDrinksToDefaultValues()
            val ctx = applicationContext
            val pm = ctx.packageManager
            val intent = pm.getLaunchIntentForPackage(ctx.packageName)
            val mainIntent = Intent.makeRestartActivityTask(intent!!.component)
            ctx.startActivity(mainIntent)
            Runtime.getRuntime().exit(0)
        }
        // leads into Edit Drinks page
        accessDrinksButton.setOnClickListener{
            val intent = Intent(this@DrinksActivity, EditDrinksActivity::class.java)
            startActivity(intent);
        }

        // returns back to previous menu
        returnButton.setOnClickListener{
            finish()
        }
    }
}
