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

class CustomAdapter(private val mList: List<ItemsViewModel>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    //create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomAdapter.ViewHolder {
        TODO("Not yet implemented")
        //infales the card_view_desgin view
        val view = LayoutInflater.from(parent.context).inflate()

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomAdapter.ViewHolder, position: Int) {
        TODO("Not yet implemented")
        val ItemsViewModel = mList[position]

        // sets the image to the imageview from our itemHolder class
        holder.imageView.setImageResource(ItemsViewModel.image)

        // sets the text to the textview from our itemHolder class
        holder.textView.text = ItemsViewModel.text
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
        return mList.size
    }

    class ViewHolder(ItemView : View) : RecyclerView.ViewHolder(ItemView){
        val mainImage : ImageView = itemView.findViewById()
        val textViewModel : TextView = itemView.findViewById()
        val heading : TextView = //
        val arrowNextPage : ImageButton
        val arrowPrevPage : ImageButton
    }
}