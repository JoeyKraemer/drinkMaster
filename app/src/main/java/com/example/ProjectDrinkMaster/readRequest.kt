package com.example.ProjectDrinkMaster

import android.os.AsyncTask
import android.util.Log
import java.net.ConnectException
import java.net.ProtocolException
import java.net.URL
import java.util.concurrent.TimeoutException

// task to read an entire page
// normal usage: readRequest(url,page).execute().get()
class readRequest(url: String, page: String) : AsyncTask<Void, Void, String>() {
    private val url = url
    private val page = page
    override fun doInBackground(vararg params: Void?): String? {
        try {
            return URL("$url$page").readText()
        } catch (e: ConnectException) {
            Log.e("readRequest", e.toString())
            return null

        } catch (e: ProtocolException) {
            Log.e("readRequest", e.toString())
            return null
        }
    }
}
