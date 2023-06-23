package com.example.ProjectDrinkMaster

import android.util.Log
import java.io.EOFException
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.ProtocolException
import java.net.URL

// task to send something to a webpage, prints response code to console
// normal usage: sendRequest(key, value).start()
class SendRequest(key: String, value: String, url: String = MainActivity.url) : Thread() {
    val url = URL("$url?$key=$value")

    override fun run() {

        Log.d("sendRequest", "sending request")

        try {
            with(url.openConnection() as HttpURLConnection) {
                requestMethod = "GET"
                Log.d(
                    "networkRequest",
                    "\nSent 'GET' request to URL : $url; Response Code : $responseCode"
                )
            }
        } catch (e: ConnectException) {
            Log.e("sendRequest", e.toString())

        } catch (e: ProtocolException) {
            Log.e("sendRequest", e.toString())

        } catch (e: EOFException) {
            Log.e("sendRequest", e.toString())
        }
    }
}
