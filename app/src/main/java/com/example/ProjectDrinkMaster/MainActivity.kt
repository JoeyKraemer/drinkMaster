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
// qrcode imports
import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import java.io.FileNotFoundException
// rating imports
import android.widget.RatingBar
import android.widget.TextView

// main page
class MainActivity : AppCompatActivity() {
    // global variables
    companion object {
        const val url = "http://192.168.0.103:5000/"
        const val fileName = "drinkValues.json"
        var errormsgs = ArrayList<Array<String>>()
        // ===== FILE I/O =====
        // reads off the drinkList, the return can be used with jsonObject.get("drink1")
        fun readOffDrinkValueFile(packageName: String = "com.example.ProjectDrinkMaster"): JSONObject {
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

        // creates an entirely new file for drinkValues.json (essentially a reset function)
        fun newDrinkValueFile(packageName: String = "com.example.ProjectDrinkMaster") {
            val jsonObject = JSONObject()
            val nOfDrinks = 4
            val nOfIngredients = 6

            var drinks = JSONObject()
            var ingredients = JSONObject()

            for (i in 1..nOfDrinks) {  // for each drink, adds default values and assigns it to drinks
                var drink = JSONObject()
                var drinkIngredients = JSONObject()
                var ingredient1 = JSONObject()

                drink.put("id", "$i") // id
                drink.put("name", "drink$i") //name
                drink.put("description", "description$i") // description

                ingredient1.put("id", (0..5).random()) //placeholder id
                ingredient1.put("amount", (1..2).random()) //placeholder amount

                drinkIngredients.put("ingredient1", ingredient1) // adds first ingredient

                drink.put("drinkIngredients", drinkIngredients) // adds ingredients to drink
                drink.put("sold", 0) // adds number of drinks sold
                drinks.put("drink$i", drink)

                drink.put("drinkIngredients", drinkIngredients) // adds ingredients to drink
                drink.put("price", 0) // price, placeholder of 0
                drink.put("sold", 0) // adds number of drinks sold
                drinks.put("drink$i", drink)

            }
            jsonObject.put("drinks", drinks)

            for (i in 1..nOfIngredients) { // assigns default ingredients to file
                var ingredient = JSONObject()

                ingredient.put("id", i) // id
                ingredient.put("name", "ingredient$i") // placeholder name
                ingredient.put("maxMl", 700) // placeholder max ml
                ingredient.put("currentMl", 700) // placeholder current ml
                ingredients.put("ingredient$i", ingredient)
            }
            jsonObject.put("ingredients", ingredients)

            val userString = jsonObject.toString()
            val fileWriter = FileWriter("/data/data/$packageName/$fileName")
            val bufferedWriter = BufferedWriter(fileWriter)
            bufferedWriter.write(userString)
            bufferedWriter.close()
        }
        // writes to drink value file, can be used to modify file. syntax: writeToDrinkValueFile(JSONObject)
        fun writeToDrinkValueFile(
            jsonObject: JSONObject,
            packageName: String = "com.example.ProjectDrinkMaster"
        ) {
            val userString = jsonObject.toString()
            val fileWriter = FileWriter("/data/data/$packageName/$fileName")
            val bufferedWriter = BufferedWriter(fileWriter)
            bufferedWriter.write(userString)
            bufferedWriter.close()
        }

        // rewrites the drink value file to have the default names and descriptions
        fun resetDrinksToDefaultValues() {
            val data = readOffDrinkValueFile()

            val description1 =
                "Mint syrup is a sweet liquid made by extracting the essence and flavors from fresh mint leaves and combining them with sugar and water. It is a popular ingredient used in various culinary applications, particularly in beverages and desserts.\n" +
                        "\n" +
                        "To make mint syrup, fresh mint leaves are typically steeped in a simple syrup solution, which is a mixture of water and sugar heated until the sugar dissolves. This process allows the mint leaves to infuse their aromatic and refreshing qualities into the syrup. The syrup is then strained to remove the mint leaves, resulting in a smooth and flavorful liquid."

            val description2 =
                "Strawberry syrup drink is a refreshing and fruity beverage made with a syrup derived from strawberries. It typically involves blending or mashing fresh strawberries and combining them with sugar and water to create a sweet and flavorful syrup.\n" +
                        "\n" +
                        "To prepare strawberry syrup drink, ripe strawberries are washed, hulled (removing the green stem), and then mashed or blended into a puree. The puree is then combined with sugar and water in a saucepan and simmered over low heat until the sugar completely dissolves and the mixture thickens slightly. The resulting syrup is strained to remove any solids or seeds, leaving a smooth and vibrant strawberry-flavored liquid."

            val description3 =
                "Lemon syrup drink is a refreshing and tangy beverage made with a sweet syrup infused with the essence of lemons. It offers a bright and zesty flavor that is both thirst-quenching and satisfying.\n" +
                        "\n" +
                        "To create lemon syrup drink, the first step involves making a simple syrup by dissolving sugar in water over heat until the sugar completely dissolves. Once the simple syrup is ready, freshly squeezed lemon juice is added to the mixture. This combination of sweet and tart flavors creates a balanced and delightful taste."

            val description4 =
                "Water is a clear, odorless, and tasteless liquid that is essential for the survival and well-being of all living organisms. It is the most basic and fundamental drink, often referred to as the \"universal solvent.\" Drinking water is crucial for maintaining proper hydration and supporting various bodily functions.\n" +
                        "\n" +
                        "Pure water, in its natural form, contains no additives or flavors. It is composed of hydrogen and oxygen molecules (H2O) and is known for its ability to dissolve many substances, making it a great medium for transporting nutrients throughout the body."

            data.getJSONObject("drinks").getJSONObject("drink1").put("name", "Mint Syrup")
            data.getJSONObject("drinks").getJSONObject("drink1").put("description", description1)

            data.getJSONObject("drinks").getJSONObject("drink2").put("name", "Strawberry Syrup")
            data.getJSONObject("drinks").getJSONObject("drink2").put("description", description2)

            data.getJSONObject("drinks").getJSONObject("drink3").put("name", "Lemonade")
            data.getJSONObject("drinks").getJSONObject("drink3").put("description", description3)

            data.getJSONObject("drinks").getJSONObject("drink4").put("name", "Water")
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
    private lateinit var qr_code: ImageView
    private var last_drink_ordered: Int = 1

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

        // sets a onClickListener for the buttons on every drink page.
        // these send a request to server and call the functions: getGin(), getLemonade(), getRum() and getCoke() respectively
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

        // setting the button to get into the admin page
        val mintIcon = findViewById<ImageView>(R.id.mint)
        mintIcon.setOnClickListener { view ->
            val intent = Intent(this@MainActivity, AdminActivity::class.java)
            startActivity(intent)
        }

        // error finder thread. pings DRINKMASTER server every 5 seconds, then stores every error into errormsgs
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

        // updates user position in code whenever the user scrolls horizontally
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
            }
        })
    }

    // initializes and shows the "please confirm your drink ; yes, no" pop-up
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

    // initializes and shows the progress bar pop-up
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

    // initializes and shows the "your drink has been finished" pop-up
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

    // initializes and shows the qr code and receipt pop-up
    private fun receiptPopUpBox() {
        val builder = AlertDialog.Builder(this)
        val customView = LayoutInflater.from(this).inflate(R.layout.receipt_pop_up, null)
        builder.setView(customView)
        val dialog = builder.create()
        qr_code = customView.findViewById<ImageView>(R.id.qr_code)//qr code

        val reviewButton = customView.findViewById<Button>(R.id.button_leave_review) // Replace `your_button_id` with the actual ID of your button
        reviewButton.setOnClickListener {
            dialog.dismiss() // Dismiss the receipt dialog
            RatingBarPopUpBox() // Open the RatingBar popup
        }

        val data = readOffDrinkValueFile()
        val drinkName =
            data.getJSONObject("drinks").getJSONObject("drink$last_drink_ordered").getString("name")
        val drinkPrice =
            data.getJSONObject("drinks").getJSONObject("drink$last_drink_ordered").getInt("price")
        val qrCodeContent =
            "ordered $drinkName : $drinkPrice euro. thank you for ordering from DrinkMaster."
        val qrCodeBitmap = generateQRCode(qrCodeContent, 300, 300)
        qr_code.setImageBitmap(qrCodeBitmap)
        dialog.show()
        customView.postDelayed({
            dialog.hide()
        }, 15000)
    }

    // generates the qr code from message
    private fun generateQRCode(content: String, width: Int, height: Int): Bitmap? {
        val qrCodeWriter = QRCodeWriter()
        try {
            val bitMatrix: BitMatrix =
                qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height)
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

    private var totalRating = 0.0
    private var ratingCount = 0

    // initializes and shows the rating bar pop-up
    private fun RatingBarPopUpBox() {
        // Create and set the custom view for the AlertDialog
        val customView = LayoutInflater.from(this).inflate(R.layout.star_review, null)
        val dialog = AlertDialog.Builder(this)
            .setView(customView)
            .create()

        // Access views from the customView rather than the activity
        val ratingBar = customView.findViewById<RatingBar>(R.id.ratingBar)
        val submitButton = customView.findViewById<Button>(R.id.button_rating)
        val averageRatingView = customView.findViewById<TextView>(R.id.average_rating_view)

        submitButton.setOnClickListener {
            val rating = ratingBar.rating
            totalRating += rating
            ratingCount++
            val averageRating = totalRating / ratingCount
            averageRatingView.text = "Average Rating: $averageRating"
            customView.postDelayed({
                dialog.hide()
            }, 3000)  // Close the dialog after the rating is submitted
        }

        // Show the dialog
        dialog.show()
    }

    
    //the following functions get executed when a drink button gets pressed. then will add +1 to amount sold per drink.
    fun getGin() {
        addOneToDrinkValue(1)
        last_drink_ordered = 1
    }

    fun getRum() {
        addOneToDrinkValue(3)
        last_drink_ordered = 3
    }

    fun getLemmonade() {
        addOneToDrinkValue(2)
        last_drink_ordered = 2
    }

    fun getCoke() {
        addOneToDrinkValue(4)
        last_drink_ordered = 4
    }

    // add +1 to a drink. "drink" is an int from 1 to 4 corresponding to drink1 to drink4
    private fun addOneToDrinkValue(drink: Int) {
        val jsonObject = readOffDrinkValueFile()
        val value =
            jsonObject.getJSONObject("drinks").getJSONObject("drink$drink").getInt("sold") + 1
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

        // reads off values from json file, will reset the to default values if an error occurs
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

            data.getJSONObject("drink1").getInt("price")


        } catch (e: JSONException) {

            newDrinkValueFile()
            resetDrinksToDefaultValues()

            prepareDifferentDrinks()
            return
        } catch (e: FileNotFoundException) {
            newDrinkValueFile()
            resetDrinksToDefaultValues()

            prepareDifferentDrinks()
            return
        }

        // adds drink names and descriptions to UI
        var drink = ItemsViewModel(
            desc1,
            R.drawable.mint_drink,
            name1,
            R.drawable.ordergintonic
        )
        drinkList.add(drink)
        drink = ItemsViewModel(
            desc2,
            R.drawable.straberry_drink,
            name2,
            R.drawable.ordercolalemon
        )
        drinkList.add(drink)
        drink = ItemsViewModel(
            desc3,
            R.drawable.lemonade,
            name3,
            R.drawable.orderrumcola
        )
        drinkList.add(drink)
        drink = ItemsViewModel(
            desc4,
            R.drawable.water,
            name4,
            R.drawable.orderlemonade
        )
        drinkList.add(drink)
        drinkAdapter.notifyDataSetChanged()

    }

    // for progress bar
    fun getTime(): Long {
        return time
    }
}
