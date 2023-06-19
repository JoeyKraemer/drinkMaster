package com.example.ProjectDrinkMaster
public lateinit var mListener: OnOrderButtonPress
interface OnOrderButtonPress {

    fun onPress(): Void;
    fun onItemClick(position: Int)
    fun setOnItemClickListener(listener: OnOrderButtonPress){
        mListener = listener
    }

    public fun getMListener() : OnOrderButtonPress{
        return mListener
    }
}