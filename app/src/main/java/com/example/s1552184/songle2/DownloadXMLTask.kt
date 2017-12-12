package com.example.s1552184.songle2
import android.content.res.Resources
import android.os.AsyncTask
import android.util.Log
import org.xmlpull.v1.XmlPullParserException
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

/**
 * Created by s1552184 on 08/11/17.
 */
class DownloadXmlTask(private val caller : DownloadCompleteListener) :
        AsyncTask<String, Void, String>() {
    override fun doInBackground(vararg urls: String): String {
        return try {
            loadXmlFromNetwork(urls[0])
        } catch (e: IOException) {
            "Unable to load content. Check your network connection"
        } catch (e: XmlPullParserException) {
            "Error parsing XML "
        }
    }
    private fun loadXmlFromNetwork(urlString: String): String {
        val stream = downloadUrl(urlString)
        return stream
    }
    @Throws(IOException::class)
    private fun downloadUrl(urlString: String): String {
        val url = URL(urlString)
        val input = url.openStream()
        val reader = BufferedReader(InputStreamReader(input))
        val result=StringBuilder()
        var line:String? = reader.readLine()
        while(line!=null) {
            result.append(line + "\n")
            line = reader.readLine()

        }
        return result.toString()
    }
    override fun onPostExecute(result: String) {
        caller.downloadComplete(result)
    }
}


interface DownloadCompleteListener {
    fun downloadComplete(result : String)
}