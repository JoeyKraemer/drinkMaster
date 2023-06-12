package com.example.ProjectDrinkMaster

import android.annotation.SuppressLint
import  android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ProjectDrinkMaster.sendRequest

class MainActivity : AppCompatActivity() {

    // global variables
    companion object{
        const val url = "http://192.168.0.102:5000/"
    }

    private lateinit var binding: Activity
    //recycling view
    private lateinit var newRecyclerView: RecyclerView
    private lateinit var newArrayList: ArrayList<Rum_coke>
    lateinit var  heading : Array<String>
    lateinit var news : Array<Rum_coke>
    lateinit var imageId : Array<Int>
    lateinit var text : Array<String>
    private var drinkList = ArrayList<ItemsViewModel>()
    private lateinit var drinkAdapter : CustomAdapter

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var showPopUp = findViewById<ImageButton>(R.id.order)
        var countMocktail = 0
        var countRumCoke = 0
        var countLemonade = 0
        var countCokeLemon = 0

        //getting the recyclerview by its id
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        drinkAdapter = CustomAdapter(drinkList)
        //creating the layout
        var test = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        recyclerView.adapter = drinkAdapter
        prepareDiffernetDrinks()
        showPopUp.setOnClickListener {
            showPop()
        }

        val mintIcon = findViewById<ImageView>(R.id.mint)
        mintIcon.setOnClickListener{ view ->
            val intent = Intent(this@MainActivity, AdminActivity::class.java)
            startActivity(intent);
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
        yesButton.setOnClickListener{
            dialog.hide()
            sendRequest("action", "calibration").execute()
            TODO("change above request to drink request (alongside other options)")
            confirmationPopUp()
        }
        noButton.setOnClickListener{
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
        },10000)
    }

    private fun finishedPopUpBox() {
        val builder = AlertDialog.Builder(this)
        val customView = LayoutInflater.from(this).inflate(R.layout.finished_box, null)
        builder.setView(customView)
        val dialog = builder.create()
        dialog.show()
        customView.postDelayed(
            {dialog.hide()},5000)
    }

    private fun prepareDiffernetDrinks(){
        var drink = ItemsViewModel("Gin and Tonic, a beloved classic cocktail, is a delightful fusion of gin's botanical flavors and the refreshing effervescence of tonic water. This iconic drink originated in the 19th century as a malaria-fighting elixir for British soldiers in India, and it has since become a timeless favorite worldwide.\n" +
                "\n" +
                "Crafting a Gin and Tonic is simple yet satisfying. It typically involves combining gin, tonic water, and a hint of citrus, usually a slice of lime or lemon. The real magic happens when the flavors mingle, creating a crisp and invigorating beverage that is perfect for any occasion.",R.drawable.gin,"Gin n toinc")
        drinkList.add(drink)
        drink = ItemsViewModel("Coke and Cola Max are iconic carbonated beverages that deliver the classic cola experience with their unique twists. Coke captures hearts with its rich, caramel-like flavor and refreshing effervescence, while Cola Max offers an intense taste with zero sugar and added caffeine. Whether you crave the timeless taste of Coke or the extra kick of Cola Max, both beverages provide a satisfying cola experience. Indulge in the perfect duo of Coke and Cola Max for a refreshing and energizing treat.",R.drawable.coke,"Coke Lemon")
        drinkList.add(drink)
        drink = ItemsViewModel("Rum and Coke is a timeless cocktail that brings together the smooth, rich flavors of rum with the crisp, effervescence of cola. This simple yet delightful drink has been enjoyed by cocktail enthusiasts for decades.",R.drawable.rum,"Rum cola")
        drinkList.add(drink)
        drink = ItemsViewModel("Lemonade is a classic and refreshing beverage that embodies the bright and tangy flavors of fresh lemons. With its simple yet irresistible blend of lemon juice, water, and sweetener, lemonade has been a beloved thirst-quencher for generations.",R.drawable.lemonade,"Lemonde")
        drinkList.add(drink)
        drinkAdapter.notifyDataSetChanged()
    }


}


