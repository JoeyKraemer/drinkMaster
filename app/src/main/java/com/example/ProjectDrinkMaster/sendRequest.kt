package com.example.ProjectDrinkMaster;

import android.os.AsyncTask
import android.util.Log
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

//TODO("use async method which is not deprecated")

// task to send something to a webpage
// normal useage: sendRequest(url, key, value).execute()
class sendRequest (key: String, value: String, url: String = MainActivity.url) : AsyncTask<Void, Void, String>() {
    val url = URL("$url?$key=$value")
    @Deprecated("Deprecated in Java")
    override fun doInBackground(vararg params: Void?): String {
        Log.d("networkRequest","sending request")

        with(url.openConnection() as HttpURLConnection){
            requestMethod = "GET"
            //Log.d("networkRequest","\nSent 'GET' request to URL : $url; Response Code : $responseCode")
        }
        return "done"
    }
}