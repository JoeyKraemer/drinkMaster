package com.example.tileandbackground

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class AdminActivity : AppCompatActivity() {



    // size of graph in dp
    val graphHeight = 400

    private val graphBarLengths = ArrayList<Int>(4)

    private val graphBars = ArrayList<ImageView>(4)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_admin)

        /*
        // get buttons
        val rebootPiButton = findViewById<Button>(R.id.rebootPiButton)
        val rebootMachineButton = findViewById<Button>(R.id.rebootMachineButton)
        val calibrateButton = findViewById<Button>(R.id.calibrateButton)
         */


        // dynamically get graph bar images
        for(i in graphBars.indices){
            graphBars[i] = findViewById<ImageView>(resources.getIdentifier("graphBar$i","id", getPackageName()))
        }


        // import array of amount of drinks sold from main, if it fails, all values are 0
        val importedArray = intent.getIntArrayExtra("drinkValues")
        if (importedArray != null) {
            for(i in graphBarLengths.indices){
                graphBarLengths[i] = importedArray[i]
            }
        }

        /*
        rebootPiButton.setOnClickListener(){

        }
        rebootMachineButton.setOnClickListener(){

        }
        calibrateButton.setOnClickListener(){

        }
        */

        resizeGraph()
    }

    // will change the graph bar image sizes, depending on the values in graphBarLengths (designed in use with "fitXY")
    private fun resizeGraph() {

        // find the highest number
        var highest = 0
        for (i in graphBarLengths){
            if (graphBarLengths[i] > highest){
                highest = graphBarLengths[i]
            }
        }
        if (highest == 0){
            return
        }
        var sizes = calcScaled(graphHeight, highest, graphBarLengths)

        for(i in sizes.indices){
            graphBars[i].layoutParams.height = sizes[i]
            // find a way to update the view if it doesn't.
        }

    }

    // calculate the actual size of the element based on part/whole*max
    private fun calcScaled(height: Int, highest: Int, values: ArrayList<Int>): ArrayList<Int>{

        var sizes = ArrayList<Int>(values.size)
        for(i in values.indices){
            sizes[i] = (values[i]/highest*height).toInt()
        }

        return sizes
    }
}
