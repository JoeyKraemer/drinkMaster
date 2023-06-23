package com.example.ProjectDrinkMaster

lateinit var mListener: OnOrderButtonPress

interface OnOrderButtonPress {
    fun onPress(): Void
    fun onItemClick(position: Int)
    fun setOnItemClickListener(listener: OnOrderButtonPress) {
        mListener = listener
    }
    fun getMListener(): OnOrderButtonPress {
        return mListener
    }
}
