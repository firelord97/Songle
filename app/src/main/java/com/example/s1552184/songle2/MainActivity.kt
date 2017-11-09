package com.example.s1552184.songle2

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var receiver = NetworkReceiver()
    var networkPref = "wifi"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        this.registerReceiver(receiver, filter)
        button3.setOnClickListener(){
            val intent = Intent(this, HowToPlayActivity::class.java)
            startActivity(intent)}
        play.setOnClickListener(){
            val intent = Intent(this, ListSelect::class.java)
            startActivity(intent)}
    }
    private inner class NetworkReceiver : BroadcastReceiver()
    { override fun onReceive(context: Context, intent: Intent)
        {
            val connMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connMgr.activeNetworkInfo
            if (networkPref == "wifi" && networkInfo?.type == ConnectivityManager.TYPE_WIFI)
            {
            }
            else if (networkPref == "any" && networkInfo != null)
            {
// Have a network connection and permission, so use data
            }
            else
            {
            }
        }
    }
    }
