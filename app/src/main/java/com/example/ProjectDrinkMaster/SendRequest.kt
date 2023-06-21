package com.example.ProjectDrinkMaster

import android.os.AsyncTask
import android.util.Log
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.ProtocolException
import java.net.URL
import kotlin.system.exitProcess

// task to send something to a webpage
// normal useage: sendRequest(key, value).start()
class SendRequest(key: String, value: String, url: String = MainActivity.url) : Thread() {
    val url = URL("$url?$key=$value")
    val recivedValue = value

    override fun run() {

        Log.d("sendRequest", "sending request")

        try {
            with(url.openConnection() as HttpURLConnection) {
                requestMethod = "GET"
                Log.d(
                    "networkRequest",
                    "\nSent 'GET' request to URL : $url; Response Code : $responseCode"
                )
                if (recivedValue === "rebootPi"){
                    exitProcess(0)
                }

            }
        } catch (e: ConnectException) {
            Log.e("sendRequest", e.toString())

        } catch (e: ProtocolException) {
            Log.e("sendRequest", e.toString())

        }


    }

}
