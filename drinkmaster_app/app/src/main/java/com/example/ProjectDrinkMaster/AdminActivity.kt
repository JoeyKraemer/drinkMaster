package com.example.ProjectDrinkMaster

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry

// Admin panel page
class AdminActivity : AppCompatActivity() {

    // size of graph in dp
    val pincode = 1234

    private lateinit var dialog: AlertDialog
    private var keycodeDigitElements = ArrayList<EditText>(4)
    private val graphBarLengths = ArrayList<Int>(4)
    private var graphBars = ArrayList<ImageView>(4)
    lateinit var barChart: BarChart
    lateinit var barData: BarData
    lateinit var barDataSet: BarDataSet
    lateinit var barEntriesList: ArrayList<BarEntry>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_admin)

        // ===== INIT BUTTONS ======
        val rebootPiButton = findViewById<ImageButton>(R.id.RestartRaspberryPiButton)
        val rebootMachineButton = findViewById<ImageButton>(R.id.RestartMachineButton)
        val calibrateButton = findViewById<ImageButton>(R.id.CalibrateMachineButton)
        val accessDrinksButton = findViewById<ImageButton>(R.id.accessDrinksButton)
        val openCup = findViewById<ImageButton>(R.id.ReleaseCupButton)
        val backButton = findViewById<ImageButton>(R.id.BackButton)

        rebootPiButton.setOnClickListener {
            SendRequest("action", "rebootPi").start()
        }
        rebootMachineButton.setOnClickListener {
            SendRequest("action", "rebootPlatform").start()
        }

        calibrateButton.setOnClickListener {
            SendRequest("action", "calibration").start()
        }

        openCup.setOnClickListener {

            SendRequest("action", "pushUp")
        }
        backButton.setOnClickListener {
            finish()
        }

        accessDrinksButton.setOnClickListener {
            val intent = Intent(this@AdminActivity, DrinksActivity::class.java)
            startActivity(intent)
        }
        // ===== INIT KEYCODE ======
        // create pop up password
        showPasswordDialog()

        // ===== INIT GRAPH ======
        // get amount of drinks sold for graph
        val data = MainActivity.readOffDrinkValueFile().getJSONObject("drinks")
        for (i in (1..4)) {
            graphBarLengths += data.getJSONObject("drink$i").getInt("sold")
        }

        // create graph
        barChart = findViewById(R.id.idBarChart)
        getBarChartData()
        barDataSet = BarDataSet(barEntriesList, "Bar Chart Data")
        barData = BarData(barDataSet)
        barChart.data = barData
        barDataSet.valueTextColor = Color.BLACK
        barDataSet.color = resources.getColor(R.color.darkBeige)
        barDataSet.valueTextSize = 16f
        barChart.description.isEnabled = false
    }

    // executes after the pin is entered (extension of OnCreate)
    fun onPinEntered() {
        if (MainActivity.errormsgs.isNotEmpty()) {
            var text = MainActivity.errormsgs.last()[0] + " " + MainActivity.errormsgs.last()[1]
        }
    }

    // ===== GRAPH LOGIC =====
    private fun getBarChartData() {
        barEntriesList = ArrayList()
        // on below line we are adding data
        // to our bar entries list
        barEntriesList.add(BarEntry(1f, graphBarLengths[0].toFloat()))
        barEntriesList.add(BarEntry(2f, graphBarLengths[1].toFloat()))
        barEntriesList.add(BarEntry(3f, graphBarLengths[2].toFloat()))
        barEntriesList.add(BarEntry(4f, graphBarLengths[3].toFloat()))
    }

    // ===== PINCODE LOGIC =====
    // text watcher for the pinCode popup
    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

            var numTemp: String

            // iterates through every value in keycodeDigitSize
            (0 until keycodeDigitElements.size)
                .forEach { i ->
                    if (s != null) {
                        if (s.isBlank()) {
                            return
                        }
                    }

                    if (s === keycodeDigitElements[i].editableText) {
                        if (i != keycodeDigitElements.size - 1) { // if not last char
                            keycodeDigitElements[i + 1].requestFocus()
                            keycodeDigitElements[i + 1].setSelection(keycodeDigitElements[i + 1].length())
                            return
                        } else {
                            //Last character is inserted
                            if (validateCode(getCode())) {
                                dialog.dismiss()
                                dialog.cancel()
                                onPinEntered()
                            } else {
                                for (ii in keycodeDigitElements.indices) {
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

    // initializes and shows enter password dialog
    private fun showPasswordDialog() {
        val builder = AlertDialog.Builder(this)
        val dialogView = LayoutInflater.from(this).inflate(R.layout.pincode_pop_up, null)
        builder.setView(dialogView)
        dialog = builder.create()

        // disallow user from removing popup
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)

        // makes it so that it returns when you press the back button
        dialog.setOnKeyListener { dialog, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                finish()
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
        dialog.show()

        // add keycode digit blocks
        keycodeDigitElements += dialogView.findViewById<EditText>(R.id.code1)
        keycodeDigitElements += dialogView.findViewById<EditText>(R.id.code2)
        keycodeDigitElements += dialogView.findViewById<EditText>(R.id.code3)
        keycodeDigitElements += dialogView.findViewById<EditText>(R.id.code4)

        //add text change listener
        for (i in keycodeDigitElements.indices) {
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
        // focus back on first number
        keycodeDigitElements[0].requestFocus()
    }

    // gets text entered on keypad
    fun getCode(): String {
        var code = ""
        for (i in keycodeDigitElements.indices) {
            val digit = keycodeDigitElements[i].text.toString().trim { it <= ' ' }
            code += digit
        }
        return code
    }

    // sees if string is same as hardcoded passcode
    fun validateCode(code: String): Boolean {
        return (code == pincode.toString())
    }
}
