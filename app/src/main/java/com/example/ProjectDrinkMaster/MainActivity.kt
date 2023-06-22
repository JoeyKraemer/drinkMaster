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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.lang.Thread.sleep
import java.util.Calendar
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {

    // global variables
    companion object {
        const val url = "http://192.168.0.103:5000/"
        const val fileName = "drinkValues.json"
        var errormsgs = ArrayList<Array<String>>()


        // ===== FILE I/O =====
        // reads off the drinkList, the return can be used with jsonObject.get("drink1")

        public fun readOffDrinkValueFile(packageName: String = "com.example.ProjectDrinkMaster"): JSONObject {
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

        public fun newDrinkValueFile(packageName: String = "com.example.ProjectDrinkMaster") {
            val jsonObject = JSONObject()
            val nOfDrinks = 4
            val nOfIngredients = 6


            var drinks = JSONObject()
            var ingredients = JSONObject()


            for(i in 1..nOfDrinks){  // adds ingredients
                var drink = JSONObject()
                var drinkIngredients = JSONObject()
                var ingredient1 = JSONObject()

                drink.put("id","$i") // id
                drink.put("name","drink$i") //name
                drink.put("description", "description$i")

                ingredient1.put("id",(0..5).random()) //placeholder id
                ingredient1.put("amount",(1..2).random()) //placeholder amount

                drinkIngredients.put("ingredient1", ingredient1) // adds first ingredient

                drink.put("drinkIngredients",drinkIngredients) // adds ingredients to drink
                drink.put("sold",0) // adds number of drinks sold
                drinks.put("drink$i",drink)
            }
            jsonObject.put("drinks", drinks)

            for(i in 1 .. nOfIngredients){
                var ingredient = JSONObject()

                ingredient.put("id", i) // id
                ingredient.put("name", "ingredient$i") // placeholder name
                ingredient.put("maxMl", 700) // placeholder max ml
                ingredient.put("currentMl",700) // placeholder current ml
                ingredients.put("ingredient$i",ingredient)
            }
            jsonObject.put("ingredients", ingredients)


            val userString = jsonObject.toString()
            val fileWriter = FileWriter("/data/data/$packageName/$fileName")
            val bufferedWriter = BufferedWriter(fileWriter)
            bufferedWriter.write(userString)
            bufferedWriter.close()
        }

        // overrides drink value file
        public fun writeToDrinkValueFile(jsonObject: JSONObject, packageName: String = "com.example.ProjectDrinkMaster") {
            val userString = jsonObject.toString()
            val fileWriter = FileWriter("/data/data/$packageName/$fileName")
            val bufferedWriter = BufferedWriter(fileWriter)
            bufferedWriter.write(userString)
            bufferedWriter.close()
        }

        public fun resetDrinksToDefaultValues(){
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

    private lateinit var binding: Activity
    //recycling view
    private lateinit var newRecyclerView: RecyclerView
    lateinit var heading: Array<String>
    lateinit var imageId: Array<Int>
    lateinit var text: Array<String>
    private var drinkList = ArrayList<ItemsViewModel>()
    private lateinit var drinkAdapter: CustomAdapter
    private lateinit var getInterface: OnOrderButtonPress
    private var buttonPressed = false
    private var totalScrolledPixels = 0
    private val targetPixels = 500
    private var shouldScrollToPosition2 = false
    private var time: Long = 3000

    @SuppressLint("MissingInflatedId", "ResourceType")
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
        // our snap helper class and initializing it for our Linear Snap Helper.
        val snapHelper: SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)

        prepareDifferentDrinks()

        drinkAdapter.setOnOrderClick {
            Log.d(
                "Button pressed",
                "Button" + (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
            )
            //depending on what button has been pressed called differnet function
            if ((recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition() === 0) {
                getGin()
                SendRequest("action", "DRINK1").start()
            } else if ((recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition() === 1) {
                SendRequest("action", "DRINK2").start()
                getLemmonade()
            } else if ((recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition() === 2) {
                getRum()
                SendRequest("action", "DRINK3").start()
            } else {
                getCoke()
                SendRequest("action", "DRINK4").start()
            }
            showPop()
        }

        // setting button to get into the admin page
        val mintIcon = findViewById<ImageView>(R.id.mint)
        mintIcon.setOnClickListener { view ->
            val intent = Intent(this@MainActivity, AdminActivity::class.java)
            startActivity(intent)
        }

        var lastError = ""
        thread(
            true,
            name = "error finder"
        ) { // thread for pinging the server in order to find new errors
            while (true) {

                val pageString = readRequest(url, "").execute().get()
                if (pageString == null) {
                    Log.e("error finder", "could not get webpage, retrying in 30 seconds")
                    sleep(30000)
                    continue
                }

                val regex =
                    "<div id=(?:\"|')?error(?:\"|')? ?> ?(.*) ?</div>".toRegex()      // <div id=error> bla bla </div>
                val results = regex.find(pageString)?.groupValues

                if (results == null) {
                    Log.e("error finder", "page does not contain error window")
                } else {
                    if (results.size == 2) { // if there's something between the ><
                        if (results[1] != "") { // if result 1 (the middle of the ><) is not empty
                            if (lastError != results[1]) { // if it's not the same as the last error
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

        // check if /data/data/$packageName/$fileName exists, if not, makes a new file
        val file = File("/data/data/$packageName/$fileName")
        if (!file.exists()) {
            newDrinkValueFile()
        }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                //update the total scrolledPixels var
                totalScrolledPixels += dx
                var y = recyclerView.x
                if (totalScrolledPixels === targetPixels) {
                    shouldScrollToPosition2 = true
                    totalScrolledPixels = 0
                    //recyclerView.layoutManager?.scrollToPosition(5000)
                    (recyclerView.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(
                        1,
                        0
                    )
                }
                //output
                 Log.d("Scrool", "amounnf of pixels: $totalScrolledPixels")
            }
        })
    }

    private fun showPop() {
        //creating the alert box
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
            SendRequest("action", "test").start()
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
        }, time)
    }

    private fun finishedPopUpBox() {
        val builder = AlertDialog.Builder(this)
        val customView = LayoutInflater.from(this).inflate(R.layout.finished_box, null)
        builder.setView(customView)
        val dialog = builder.create()
        dialog.show()
        customView.postDelayed({
            dialog.hide()
            receiptPopUpBox()
        }, 5000)
    }

    private fun receiptPopUpBox() {
        val builder = AlertDialog.Builder(this)
        val customView = LayoutInflater.from(this).inflate(R.layout.receipt_pop_up, null)
        builder.setView(customView)
        val dialog = builder.create()
        dialog.show()
        customView.postDelayed({
            dialog.hide()
        }, 5000)
    }
/*
    import android.graphics.Bitmap
    import android.graphics.Color
    import android.os.Bundle
    import android.widget.ImageView
    import androidx.appcompat.app.AppCompatActivity
    import com.google.zxing.BarcodeFormat
    import com.google.zxing.WriterException
    import com.google.zxing.common.BitMatrix
    import com.google.zxing.qrcode.QRCodeWriter

        private lateinit var qr_code: ImageView

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)

            qrCodeImageView = findViewById(R.id.qr_code)

            val qrCodeContent = "DRINK1"
            val qrCodeBitmap = generateQRCode(qrCodeContent, 500, 500)
            qrCodeImageView.setImageBitmap(qrCodeBitmap)
        }

        private fun generateQRCode(content: String, width: Int, height: Int): Bitmap? {
            val qrCodeWriter = QRCodeWriter()
            try {
                val bitMatrix: BitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height)
                val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
                for (x in 0 until width) {
                    for (y in 0 until height) {
                        bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                    }
                }
                return bitmap
            } catch (e: WriterException) {
                e.printStackTrace()
            }
            return null
        }
    }*/

    //the following funtions will add 1 drink to the bars
    fun getGin() {
        addOneToDrinkValue(1)
    }

    fun getRum() {
        addOneToDrinkValue(3)
    }

    fun getLemmonade() {
        addOneToDrinkValue(2)
    }

    fun getCoke() {
        addOneToDrinkValue(4)
    }

    // add +1 to a drink. "drink" is an int from 1 to 4 corresponding to drink1 to drink4
    private fun addOneToDrinkValue(drink: Int) {
        val jsonObject = readOffDrinkValueFile()
        val value = jsonObject.getJSONObject("drinks").getJSONObject("drink$drink").getInt("sold") + 1
        jsonObject.getJSONObject("drinks").getJSONObject("drink$drink").put("sold", value)
        writeToDrinkValueFile(jsonObject)
    }

    //here we store what values are suppose to be shown
    private fun prepareDifferentDrinks() {

        var name1 = ""
        var desc1 = ""
        var name2 = ""
        var desc2 = ""
        var name3 = ""
        var desc3 = ""
        var name4 = ""
        var desc4 = ""

        try {
            val data = readOffDrinkValueFile().getJSONObject("drinks")

            name1 = data.getJSONObject("drink1").getString("name")
            desc1 = data.getJSONObject("drink1").getString("description")

            name2 = data.getJSONObject("drink2").getString("name")
            desc2 = data.getJSONObject("drink2").getString("description")

            name3 = data.getJSONObject("drink3").getString("name")
            desc3 = data.getJSONObject("drink3").getString("description")

            name4 = data.getJSONObject("drink4").getString("name")
            desc4 = data.getJSONObject("drink4").getString("description")

        }
        catch(e:JSONException){
            newDrinkValueFile()
            resetDrinksToDefaultValues()

            prepareDifferentDrinks()
            return
        }

        var drink = ItemsViewModel(
            desc1,
            R.drawable.gin,
            name1,
            R.drawable.ordergintonic
        )
        drinkList.add(drink)
        drink = ItemsViewModel(
            desc2,
            R.drawable.coke,
            name2,
            R.drawable.ordercolalemon
        )
        drinkList.add(drink)
        drink = ItemsViewModel(
            desc3,
            R.drawable.rum,
            name3,
            R.drawable.orderrumcola
        )
        drinkList.add(drink)
        drink = ItemsViewModel(
            desc4,
            R.drawable.lemonade,
            name4,
            R.drawable.orderlemonade
        )
        drinkList.add(drink)
        drinkAdapter.notifyDataSetChanged()

    }

    fun getTime(): Long {
        return time
    }

}
