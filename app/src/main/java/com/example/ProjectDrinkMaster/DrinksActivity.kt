package com.example.ProjectDrinkMaster

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.ProjectDrinkMaster.MainActivity.Companion.newDrinkValueFile
import com.example.ProjectDrinkMaster.MainActivity.Companion.readOffDrinkValueFile
import com.example.ProjectDrinkMaster.MainActivity.Companion.writeToDrinkValueFile

class DrinksActivity: AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drinks)

        val resetDrinksButton = findViewById<Button>(R.id.resetDrinksButton)
        val defaultValuesButton = findViewById<Button>(R.id.defaultValuesButton)

        resetDrinksButton.setOnClickListener{
            newDrinkValueFile()
        }
        defaultValuesButton.setOnClickListener{
            resetToDefaultValues()
        }


    }

    private fun resetToDefaultValues(){
        val data = readOffDrinkValueFile()

        val description1 = "Gin and Tonic, a beloved classic cocktail, is a delightful fusion of gin's botanical flavors and the refreshing effervescence of tonic water. This iconic drink originated in the 19th century as a malaria-fighting elixir for British soldiers in India, and it has since become a timeless favorite worldwide.\n" +
                "\n" +
                "Crafting a Gin and Tonic is simple yet satisfying. It typically involves combining gin, tonic water, and a hint of citrus, usually a slice of lime or lemon. The real magic happens when the flavors mingle, creating a crisp and invigorating beverage that is perfect for any occasion."

        val description2 = "Coke and Cola Max are iconic carbonated beverages that deliver the classic cola experience with their unique twists. Coke captures hearts with its rich, caramel-like flavor and refreshing effervescence, while Cola Max offers an intense taste with zero sugar and added caffeine. Whether you crave the timeless taste of Coke or the extra kick of Cola Max, both beverages provide a satisfying cola experience. Indulge in the perfect duo of Coke and Cola Max for a refreshing and energizing treat."

        val description3 = "Rum and Coke is a timeless cocktail that brings together the smooth, rich flavors of rum with the crisp, effervescence of cola. This simple yet delightful drink has been enjoyed by cocktail enthusiasts for decades."

        val description4 = "Lemonade is a classic and refreshing beverage that embodies the bright and tangy flavors of fresh lemons. With its simple yet irresistible blend of lemon juice, water, and sweetener, lemonade has been a beloved thirst-quencher for generations."


        data.getJSONObject("drinks").getJSONObject("drink1").put("name", "Gin n Tonic")
        data.getJSONObject("drinks").getJSONObject("drink1").put("description", description1)

        data.getJSONObject("drinks").getJSONObject("drink2").put("name", "Coke Lemon")
        data.getJSONObject("drinks").getJSONObject("drink2").put("description", description2)

        data.getJSONObject("drinks").getJSONObject("drink3").put("name", "Rum cola")
        data.getJSONObject("drinks").getJSONObject("drink3").put("description", description3)

        data.getJSONObject("drinks").getJSONObject("drink4").put("name", "Lemonade")
        data.getJSONObject("drinks").getJSONObject("drink4").put("description", description4)

        writeToDrinkValueFile(data)
    }
}