package com.example.ProjectDrinkMaster

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONObject
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.FileReader
import java.io.FileWriter
import java.lang.Thread.sleep
import java.util.Calendar
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {

    // global variables
    companion object {
        const val url = "http://192.168.0.102:5000/"
        const val fileName = "drinkValues.json"
        var errormsgs = ArrayList<Array<String>>()

        // reads off the drinkList, the return can be used with jsonObject.get("drink1")
        public fun readOffDrinkValues(packageName : String = "com.example.ProjectDrinkMaster"): JSONObject {
            val fr = FileReader("/data/data/$packageName/$fileName")
            val bfReader = BufferedReader(fr)
            val stringBuilder = StringBuilder()
            var line = bfReader.readLine()
            while (line != null) {
                stringBuilder.append(line).append("\n")
                line = bfReader.readLine()
            }
            bfReader.close()
            val response = stringBuilder.toString()

            return JSONObject(response)
        }
    }

    private lateinit var binding: Activity

    //recycling view
    private lateinit var newRecyclerView: RecyclerView
    lateinit var heading: Array<String>
    lateinit var imageId: Array<Int>
    lateinit var text: Array<String>
    private var drinkList = ArrayList<ItemsViewModel>()
    private lateinit var drinkAdapter: CustomAdapter

    // private lateinit var test : ItemsViewModel
    private var countMocktail: Int = 0
    private var countRumCoke = 0
    private var countLemonade = 0
    private var countCola = 0
    private var buttonPressed = false
    lateinit var recyclerView: RecyclerView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        var showPopUp = findViewById<ImageButton>(R.id.imageButton)
        //getting the recyclerview by its id
        drinkAdapter = CustomAdapter(drinkList)
        //creating the layout
        var test = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = drinkAdapter
        prepareDiffernetDrinks()

        drinkAdapter.setOnOrderClick {
            showPop()
        }

        // setting button to get into the admin page
        val mintIcon = findViewById<ImageView>(R.id.mint)
        mintIcon.setOnClickListener { view ->
            val intent = Intent(this@MainActivity, AdminActivity::class.java)
            startActivity(intent);
        }

        var lastError = ""
        thread(true, name="error finder") {
            while (true){

                val pageString = readRequest(MainActivity.url, "").execute().get()
                if(pageString == null){
                    Log.e("error finder", "could not get webpage, retrying in 30 seconds")
                    sleep(30000)
                    continue
                }

                val regex = "<div id=(?:\"|')?error(?:\"|')? ?> ?(.*) ?</div>".toRegex()      // <div id=error> bla bla </div>
                val results = regex.find(pageString)?.groupValues

                if(results == null) {
                    Log.e("error finder", "page does not contain error window")
                }
                else {
                    if (results.size == 2) { // if there's something between the ><
                        if (results[1] != "") { // if result 1 (the middle of the ><) is not empty
                            if(lastError != results[1]) { // if it's not the same as the last error
                                Handler(Looper.getMainLooper()).post {
                                    lastError = results[1]
                                    errormsgs.add(  // add the error message with both the date and msg
                                        arrayOf(
                                            Calendar.getInstance().time.toString(),
                                            results[1]
                                        )
                                    )
                                    }
                                }
                            }
                        }
                    }

                sleep(5000)
            }
        }
    }

    private fun showPop() {
        val builder = AlertDialog.Builder(this)
        val customView = LayoutInflater.from(this).inflate(R.layout.pop_up, null)
        builder.setView(customView)
        val dialog = builder.create()
        dialog.show()
        val noButton = customView.findViewById(R.id.buttonNo) as Button
        val yesButton = customView.findViewById(R.id.buttonYes) as Button
        yesButton.setOnClickListener {
            buttonPressed == true
            dialog.hide()
            SendRequest("action", "test").execute()
            //TODO("change above request to drink request (alongside other options)")
            confirmationPopUp()
        }
        noButton.setOnClickListener {
            dialog.hide()
        }
    }

    private fun confirmationPopUp() {
        val builder = AlertDialog.Builder(this)
        val customView = LayoutInflater.from(this).inflate(R.layout.ok_box, null)
        builder.setView(customView)
        val dialog = builder.create()
        dialog.show()
        customView.postDelayed({
            dialog.hide()
            finishedPopUpBox()
        }, 10000)
    }

    private fun finishedPopUpBox() {
        val builder = AlertDialog.Builder(this)
        val customView = LayoutInflater.from(this).inflate(R.layout.finished_box, null)
        builder.setView(customView)
        val dialog = builder.create()
        dialog.show()
        customView.postDelayed(
            { dialog.hide() }, 5000
        )
    }

    // === FILE I/O ===

    // overrides and resets the drink value file (all values become 0)
    public fun newDrinkValueFile(){
        val jsonObject = JSONObject()
        jsonObject.put("drink1", 0)
        jsonObject.put("drink2", 0)
        jsonObject.put("drink3", 0)
        jsonObject.put("drink4", 0)

        val userString = jsonObject.toString()
        val fileWriter = FileWriter("/data/data/$packageName/$fileName")
        val bufferedWriter = BufferedWriter(fileWriter)
        bufferedWriter.write(userString)
        bufferedWriter.close()
    }

    // overrides drink value file
    public fun writeToDrinkValueFile(jsonObject: JSONObject){
        val userString = jsonObject.toString()
        val fileWriter = FileWriter("/data/data/$packageName/$fileName")
        val bufferedWriter = BufferedWriter(fileWriter)
        bufferedWriter.write(userString)
        bufferedWriter.close()
    }

    // add +1 to a drink. "drink" is an int from 1 to 4 corresponding to drink1 to drink4
    public fun addOneToDrinkValue(drink:Int){
        val jsonObject = readOffDrinkValues()
        val value = jsonObject.getInt("drink$drink") + 1
        jsonObject.put("drink$drink", value)
        writeToDrinkValueFile(jsonObject)
    }

    private fun prepareDiffernetDrinks() {
        var drink = ItemsViewModel(
            "Gin and Tonic, a beloved classic cocktail, is a delightful fusion of gin's botanical flavors and the refreshing effervescence of tonic water. This iconic drink originated in the 19th century as a malaria-fighting elixir for British soldiers in India, and it has since become a timeless favorite worldwide.\n" +
                    "\n" +
                    "Crafting a Gin and Tonic is simple yet satisfying. It typically involves combining gin, tonic water, and a hint of citrus, usually a slice of lime or lemon. The real magic happens when the flavors mingle, creating a crisp and invigorating beverage that is perfect for any occasion.",
            R.drawable.gin,
            "Aada",
            R.drawable.ordergintonic
        )
        drinkList.add(drink)
        drink = ItemsViewModel(
            "Coke and Cola Max are iconic carbonated beverages that deliver the classic cola experience with their unique twists. Coke captures hearts with its rich, caramel-like flavor and refreshing effervescence, while Cola Max offers an intense taste with zero sugar and added caffeine. Whether you crave the timeless taste of Coke or the extra kick of Cola Max, both beverages provide a satisfying cola experience. Indulge in the perfect duo of Coke and Cola Max for a refreshing and energizing treat.",
            R.drawable.coke,
            "Coke Lemon",
            R.drawable.ordercolalemon
        )
        drinkList.add(drink)
        drink = ItemsViewModel(
            "Rum and Coke is a timeless cocktail that brings together the smooth, rich flavors of rum with the crisp, effervescence of cola. This simple yet delightful drink has been enjoyed by cocktail enthusiasts for decades.",
            R.drawable.rum,
            "Rum cola",
            R.drawable.orderrumcola
        )
        drinkList.add(drink)
        drink = ItemsViewModel(
            "Lemonade is a classic and refreshing beverage that embodies the bright and tangy flavors of fresh lemons. With its simple yet irresistible blend of lemon juice, water, and sweetener, lemonade has been a beloved thirst-quencher for generations.",
            R.drawable.lemonade,
            "Lemonde",
            R.drawable.orderlemonade
        )
        drinkList.add(drink)
        drinkAdapter.notifyDataSetChanged()

    }

    fun RecyclerView?.getCurrentPosition() : Int {
        return (this?.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
    }

    public fun getOrderTimsGin(): Int {
        if (buttonPressed === true) {
        if ( recyclerView.getCurrentPosition() === 0) {
                countMocktail++
                return countMocktail
            }
        }
        return countMocktail
        buttonPressed == false
    }
/*
    public fun getOrderLemoade(): Int {
        if (buttonPressed === true) {
            if (getCurrentPositon() == 1) {
                countLemonade++
                return countLemonade
            }
        }
        return countLemonade
        buttonPressed == false
    }

    public fun getOrderRum(): Int {
        if (buttonPressed === true) {
            if (getCurrentPositon() == 2) {
                countRumCoke++
                return countRumCoke
            }
        }
        return countRumCoke
        buttonPressed == false
    }

    public fun getOrderCola(): Int {
        if (buttonPressed === true) {
            if (getCurrentPositon() == 3) {
                countCola++
                return countCola
            }
        }
        return countCola
        buttonPressed == false
    }

 */

}


