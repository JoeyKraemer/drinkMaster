package com.example.tileandbackground

import android.app.Activity
import android.content.Intent
import android.os.AsyncTask
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.compose.ui.graphics.Color
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    private lateinit var binding: Activity
    var drinkValues = ArrayList<Int>(4)
    val url = "http://192.168.0.102:5000/"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //get reference to Image
        val arrow = findViewById<ImageView>(R.id.arrowChangePage)
        //click Listener
        arrow.setOnClickListener {
            //what should it do
            setContentView(R.layout.activity_main)
        }

        var showPopUp = findViewById<ImageButton>(R.id.imageButton)

        for(i in drinkValues.indices){  //placeholder
            drinkValues[i] = 0
        }

        /*
        var countMocktail = 0
        var countRumCoke = 0
        var countLemonade = 0
        var countCokeLemon = 0
        */

        showPopUp.setOnClickListener {
            showPop()
            //get reference to Image
            val arrow = findViewById<ImageView>(R.id.arrowChangePage)
            //click Listener
            arrow.setOnClickListener {
                //what should it do
                setContentView(R.layout.activity_main)
            }
        }

        val mintIcon = findViewById<ImageView>(R.id.mint)
        mintIcon.setOnClickListener{ view ->
            val intent = Intent(this@MainActivity, AdminActivity::class.java)
            intent.putExtra("drinkValues", drinkValues)
            startActivity(intent);
        }
    }


    private fun showPop() {
        val builder = AlertDialog.Builder(this)
        val customView = LayoutInflater.from(this).inflate(R.layout.pop_up, null)
        builder.setView(customView)
        val dialog = builder.create()
        dialog.show()
        val noButton = customView.findViewById(R.id.button) as Button
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

    private fun sendServerRequest(key: String, value: String){
        sendRequest(url, key, value).execute()
    }
}


