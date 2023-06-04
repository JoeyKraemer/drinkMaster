package com.example.ProjectDrinkMaster

import  android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: Activity
    //recycling view
    private lateinit var newRecyclerView: RecyclerView
    private lateinit var newArrayList: ArrayList<Rum_coke>
    lateinit var  heading : Array<String>
    lateinit var news : Array<Rum_coke>
    lateinit var imageId : Array<Int>
    lateinit var text : Array<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //get reference to Image
        val arrow = findViewById<ImageView>(R.id.arrowChangePageRigth)
        //click Listener
        arrow.setOnClickListener {
            //what should it do
            setContentView(R.layout.activity_main)
        }

        var showPopUp = findViewById<ImageButton>(R.id.orderButton)
        var countMocktail = 0
        var countRumCoke = 0
        var countLemonade = 0
        var countCokeLemon = 0
        val imageToChange = findViewById(R.id.ginPicture) as ImageView

        //defining values of what can be in each array

        imageId = arrayOf(  R.drawable.gin,
            R.drawable.lemon,
            R.drawable.coke,
            R.drawable.rum
        )

        heading = arrayOf(  "Gin n Tonic",
            "Lemonade",
            "Coke and lemon",
            "Rum")

        text = arrayOf("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged." ,
        "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged.",
        "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged.",
        "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. ")


        showPopUp.setOnClickListener {
            showPop()
            //get reference to Image
            val arrow = findViewById<ImageView>(R.id.arrowChangePageRigth)
            //click Listener
            arrow.setOnClickListener {
                //what should it do
                imageToChange.setImageResource(imageId[++])
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
        yesButton.setOnClickListener{
            dialog.hide()
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



}


