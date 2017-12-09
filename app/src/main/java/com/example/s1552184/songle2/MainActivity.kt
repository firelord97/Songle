package com.example.s1552184.songle2

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import java.io.InputStream

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val caller = MyDownloadCompleteListener()
        val downloader = DownloadXmlTask(caller)
        downloader.execute("URL")

        button3.setOnClickListener(){
            val intent = Intent(this, HowToPlayActivity::class.java)
            startActivity(intent)}
        play.setOnClickListener(){
            val intent = Intent(this, ListSelect::class.java)
            startActivity(intent)}
    }

}


class MyDownloadCompleteListener : DownloadCompleteListener{
    override fun downloadComplete(result: String) {
        val parseSongs = XMLParser()
        parseSongs.parse(result.toByteArray().inputStream())
    }

}