package com.example.tileandbackground

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {
    private lateinit var binding : Activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //get reference to Image
        val arrow = findViewById(R.id.arrowChangePage) as ImageView
        //click Listener
        arrow.setOnClickListener{
            //what should it do
            setContentView(R.layout.activity_rum_coke)
        }



       var showPopUp = findViewById(R.id.imageButton) as ImageButton
       var countMocktail = 0
       var countRumCoke = 0
       var countLemonade = 0
       var countCokeLemon = 0

        showPopUp.setOnClickListener{
            showPop()

        }





    }


    private fun showPop(){

       val builder = AlertDialog.Builder(this)
       val customView = LayoutInflater.from(this).inflate(R.layout.pop_up,null)
       builder.setView(customView)
       val dialog = builder.create()

        dialog.show()
    }
}



