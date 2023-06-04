package com.example.ProjectDrinkMaster

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.ui.layout.Layout
import androidx.recyclerview.widget.RecyclerView.ViewHolder

internal class CustomAdapter(private val mList: List<ItemsViewModel>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {
    //inner class that is created inside another clas
    internal inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val description : TextView = itemView.findViewById(R.id.recyclerViewText)
       // val image : Int  itemView.findViewById(R.id.recyclerViewImage)
        val title : TextView =  itemView.findViewById(R.id.ginTonicText)


    }
    //create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        TODO("Not yet implemented")
        //infales the card_view_desgin view
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view,parent,false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomAdapter.ViewHolder, position: Int) {
        TODO("Not yet implemented")
        val ItemsViewModel = mList[position]

        // sets the image to the imageview from our itemHolder class
        holder.description.text = ItemsViewModel.getText()
       // holder.image. = ItemsViewModel.getImage()
        holder.title.text = ItemsViewModel.getTitle()


    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
        return mList.size
    }


}