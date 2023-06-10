package com.example.ProjectDrinkMaster

import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.AlertDialog
import androidx.appcompat.app.AlertDialog
import com.example.ProjectDrinkMaster.R
import com.example.ProjectDrinkMaster.readRequest
import kotlin.random.Random

class AdminActivity : AppCompatActivity() {
    val url = "http://192.168.0.102:5000/"

    private val editTextArray: ArrayList<EditText> = ArrayList(NUM_OF_DIGITS)
    companion object {
        const val NUM_OF_DIGITS = 4
    }

    // size of graph in dp
    val graphHeight = 600

    val pincode = 7338

    private lateinit var dialog: AlertDialog

    private var keycodeDigitElements = ArrayList<EditText>(4)

    private val graphBarLengths = ArrayList<Int>(4)

    private var graphBars = ArrayList<ImageView>(4)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_admin)

        // ===== BUTTONS ======
        /*

        // get buttons
        val rebootPiButton = findViewById<Button>(R.id.rebootPiButton)
        val rebootMachineButton = findViewById<Button>(R.id.rebootMachineButton)
        val calibrateButton = findViewById<Button>(R.id.calibrateButton)
         */

        /*
        rebootPiButton.setOnClickListener(){

        }
        rebootMachineButton.setOnClickListener(){

        }
        calibrateButton.setOnClickListener(){

        }
        */

        // ===== KEYCODE ======
        // create pop up password
        showPasswordDialog()


        // ===== GRAPH ======
        // randomly create values for graph

        for(i in (0..3)){
            graphBarLengths += (0..10).random()
        }

        graphBars += findViewById<ImageView>(R.id.drinkBar0)
        graphBars += findViewById<ImageView>(R.id.drinkBar1)
        graphBars += findViewById<ImageView>(R.id.drinkBar2)
        graphBars += findViewById<ImageView>(R.id.drinkBar3)

        // add values to graph
        resizeGraph()
    }



    // will change the graph bar image sizes, depending on the values in graphBarLengths (designed in use with "fitXY")
    private fun resizeGraph() {
        // find the highest number
        var highest = 0
        for (i in graphBarLengths.indices){
            if (graphBarLengths[i] > highest){
                highest = graphBarLengths[i]
            }
        }
        if (highest == 0){
            return
        }
        var sizes = calcScaled(graphHeight, highest, graphBarLengths)

        for(i in sizes.indices){
            if(sizes[i] == 0){
                sizes[i] = 10
            }
            graphBars[i].layoutParams.height = sizes[i]
            // find a way to update the view if it doesn't.
        }
    }

    // calculate the actual size of the element based on part/whole*max
    private fun calcScaled(height: Int, highest: Int, values: ArrayList<Int>): ArrayList<Int>{
        var sizes = ArrayList<Int>(values.size)
        for(i in values.indices){
            Log.d("math", values[i].toDouble().toString() + " / " + highest.toString() + " * " + height + " = ")
            Log.d("math",(values[i].toDouble()/highest*height).toString())
            sizes += (values[i].toDouble()/highest*height).toInt()
        }
        return sizes
    }



    private fun readPage(): String{
        var hello = readRequest(url,"").execute().get()
        return hello
    }



    private val textWatcher = object : TextWatcher{
        override fun afterTextChanged(s: Editable?) {

            var numTemp : String

            (0 until keycodeDigitElements.size) // iterates through every value in keycodeDigitSize
                .forEach { i ->

                    if (s != null) {
                        if (s.isBlank()) {
                            return
                        }
                    }

                    if (s === keycodeDigitElements[i].editableText) {
                        if (i != keycodeDigitElements.size - 1) { //not last char
                            keycodeDigitElements[i + 1].requestFocus()
                            keycodeDigitElements[i + 1].setSelection(keycodeDigitElements[i + 1].length())
                            return
                        } else {
                            //Last character is inserted
                            if(validateCode(getCode())){
                                dialog.hide()
                            }
                            else{
                                for(ii in keycodeDigitElements.indices){
                                    keycodeDigitElements[ii].setText("")
                                }
                                keycodeDigitElements[0].requestFocus()
                            }
                        }
                    }
                }
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }
    }

    private fun showPasswordDialog(){
        val builder = AlertDialog.Builder(this)
        val dialogView = LayoutInflater.from(this).inflate(R.layout.pincode_pop_up, null)
        builder.setView(dialogView)
        dialog = builder.create()
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show()

        // add keycode digit blocks
        keycodeDigitElements += dialogView.findViewById<EditText>(R.id.code1)
        keycodeDigitElements += dialogView.findViewById<EditText>(R.id.code2)
        keycodeDigitElements += dialogView.findViewById<EditText>(R.id.code3)
        keycodeDigitElements += dialogView.findViewById<EditText>(R.id.code4)

        //add text change listener
        for(i in keycodeDigitElements.indices){
            keycodeDigitElements[i].addTextChangedListener(textWatcher)

            // add backspace key listener
            keycodeDigitElements[i].setOnKeyListener { _, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN) {
                    //backspace
                    if (i != 0) { //Don't implement for first digit
                        keycodeDigitElements[i - 1].requestFocus()
                        keycodeDigitElements[i - 1]
                            .setSelection(keycodeDigitElements[i - 1].length())
                    }
                }
                false
            }
        }

        keycodeDigitElements[0].requestFocus()
    }

    fun getCode() : String{
        var code = ""
        for (i in keycodeDigitElements.indices){
            val digit = keycodeDigitElements[i].text.toString().trim { it <= ' ' }
            code += digit
        }
        return code
    }

    fun validateCode(code:String) : Boolean{
        return (code == pincode.toString())
    }
}
