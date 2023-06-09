package com.example.ProjectDrinkMaster

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.ProjectDrinkMaster.MainActivity.Companion.readOffDrinkValueFile
import com.example.ProjectDrinkMaster.MainActivity.Companion.writeToDrinkValueFile

class EditDrinksActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_drinks)

        val drinkTitles = ArrayList<EditText>()
        val drinkDescs = ArrayList<EditText>()

        // add views to drinkTitles and drinkDescriptions respectively
        drinkTitles += findViewById<EditText>(R.id.editableTextDrinkTitle1)
        drinkTitles += findViewById<EditText>(R.id.editableTextDrinkTitle2)
        drinkTitles += findViewById<EditText>(R.id.editableTextDrinkTitle3)
        drinkTitles += findViewById<EditText>(R.id.editableTextDrinkTitle4)

        drinkDescs += findViewById<EditText>(R.id.editableTextDrinkDescription1)
        drinkDescs += findViewById<EditText>(R.id.editableTextDrinkDescription2)
        drinkDescs += findViewById<EditText>(R.id.editableTextDrinkDescription3)
        drinkDescs += findViewById<EditText>(R.id.editableTextDrinkDescription4)

        val confirmButton = findViewById<ImageView>(R.id.okButton)

        var data = readOffDrinkValueFile()

        // puts values from drinkValues.json into the editText views
        for (i in drinkTitles.indices) {
            drinkTitles[i].setText(
                data.getJSONObject("drinks").getJSONObject("drink${i + 1}").getString("name")
            )
            drinkDescs[i].setText(
                data.getJSONObject("drinks").getJSONObject("drink${i + 1}").getString("description")
            )
        }

        // when this button is pressed, it will overwrite all data from text fields into drinkValues.json . will then restart app
        confirmButton.setOnClickListener {
            for (i in drinkTitles.indices) {
                data.getJSONObject("drinks").getJSONObject("drink${i + 1}")
                    .put("name", drinkTitles[i].text)
                data.getJSONObject("drinks").getJSONObject("drink${i + 1}")
                    .put("description", drinkDescs[i].text)
            }
            writeToDrinkValueFile(data)
            finish()
            val ctx = applicationContext
            val pm = ctx.packageManager
            val intent = pm.getLaunchIntentForPackage(ctx.packageName)
            val mainIntent = Intent.makeRestartActivityTask(intent!!.component)
            ctx.startActivity(mainIntent)
            Runtime.getRuntime().exit(0)
        }
    }
}

