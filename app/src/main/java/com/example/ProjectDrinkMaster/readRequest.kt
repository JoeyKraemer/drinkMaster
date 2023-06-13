package com.example.ProjectDrinkMaster;

import android.os.AsyncTask
import java.net.URL

// task to read an entire page
// normal usage: readRequest(url,page).execute().get()
class readRequest (url: String, page: String) : AsyncTask<Void, Void, String>() {
    private val url = url
    private val page = page
    private var result = ""
    override fun doInBackground(vararg params: Void?): String {
        result = URL("$url$page").readText()
        return result
    }
    fun getResult(): String{
        return result
    }
}