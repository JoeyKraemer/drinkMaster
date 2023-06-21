package com.example.ProjectDrinkMaster

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.ProjectDrinkMaster.MainActivity.Companion.readOffDrinkValueFile
import com.example.ProjectDrinkMaster.MainActivity.Companion.writeToDrinkValueFile

class EditDrinksActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_drinks)

        val drinkTitles = ArrayList<EditText>()
        val drinkDescs = ArrayList<EditText>()

        drinkTitles += findViewById<EditText>(R.id.editableTextDrinkTitle1)
        drinkTitles += findViewById<EditText>(R.id.editableTextDrinkTitle2)
        drinkTitles += findViewById<EditText>(R.id.editableTextDrinkTitle3)
        drinkTitles += findViewById<EditText>(R.id.editableTextDrinkTitle4)

        drinkDescs += findViewById<EditText>(R.id.editableTextDrinkDescription1)
        drinkDescs += findViewById<EditText>(R.id.editableTextDrinkDescription2)
        drinkDescs += findViewById<EditText>(R.id.editableTextDrinkDescription3)
        drinkDescs += findViewById<EditText>(R.id.editableTextDrinkDescription4)

        val confirmButton = findViewById<Button>(R.id.confirmDrinkModificationButton)

        var data = readOffDrinkValueFile()

        for (i in drinkTitles.indices){
            drinkTitles[i].setText(data.getJSONObject("drinks").getJSONObject("drink${i+1}").getString("name"))
            drinkDescs[i].setText(data.getJSONObject("drinks").getJSONObject("drink${i+1}").getString("description"))
        }

        confirmButton.setOnClickListener{
            for (i in drinkTitles.indices){
                data.getJSONObject("drinks").getJSONObject("drink${i+1}").put("name", drinkTitles[i].text)
                data.getJSONObject("drinks").getJSONObject("drink${i+1}").put("description", drinkDescs[i].text)
            }
            writeToDrinkValueFile(data)
            finish()
        }





    }


}