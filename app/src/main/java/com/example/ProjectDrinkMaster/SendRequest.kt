package com.example.ProjectDrinkMaster;

import android.os.AsyncTask
import android.util.Log
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.ProtocolException
import java.net.URL

//TODO("use async method which is not deprecated")

// task to send something to a webpage
// normal useage: sendRequest(url, key, value).execute()
class SendRequest (key: String, value: String, url: String = MainActivity.url) : AsyncTask<Void, Void, String>() {
    val url = URL("$url?$key=$value")
    @Deprecated("Deprecated in Java")
    override fun doInBackground(vararg params: Void?): String? {
        Log.d("sendRequest","sending request")

        try {
            with(url.openConnection() as HttpURLConnection){
                requestMethod = "GET"
                Log.d("networkRequest","\nSent 'GET' request to URL : $url; Response Code : $responseCode")

            }
            return "success"
        }
        catch (e: ConnectException) {
            Log.e("sendRequest", e.toString())
            return "readRequest failure"

        } catch (e: ProtocolException){
            Log.e("sendRequest", e.toString())
            return "protocolException failure"
        }
    }
}