package com.example.tileandbackground

import android.os.AsyncTask
import android.util.Log
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

// task to send something to a webpage
class sendRequest (url: String, key: String, value: String) : AsyncTask<Void, Void, String>() {
    val url = URL("$url?$key=$value")

    @Deprecated("Deprecated in Java")
    override fun doInBackground(vararg params: Void?): String {

        with(url.openConnection() as HttpURLConnection){
            requestMethod = "GET"
            Log.d("networkRequest","\nSent 'GET' request to URL : $url; Response Code : $responseCode")

        }
        return "done"
    }

}