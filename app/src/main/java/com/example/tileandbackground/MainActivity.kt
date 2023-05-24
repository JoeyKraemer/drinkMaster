package com.example.tileandbackground

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.compose.ui.graphics.Color

class MainActivity : AppCompatActivity() {
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


    }

    private fun showPop(view: View){

        val popUp = PopupMenu(this,view)
        popUp.inflate(R.menu.popup)

    }
}



