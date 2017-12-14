package com.example.s1552184.songle2

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.io.InputStream
var songs = ArrayList<Song>()

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
// The BroadcastReceiver that tracks network connectivity changes.
         var receiver = NetworkReceiver()
// Register BroadcastReceiver to track connection changes.
            val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
            this.registerReceiver(receiver, filter)
        val caller = MyDownloadCompleteListener()
        val downloader = DownloadXmlTask(caller)
        downloader.execute("http://www.inf.ed.ac.uk/teaching/courses/cslp/data/songs/songs.xml")

        button3.setOnClickListener(){
            val intent = Intent(this, HowToPlayActivity::class.java)
            startActivity(intent)}
        play.setOnClickListener() {
            if (songs.size != 0) {
                var numbers = ArrayList<String>()
                var titles = ArrayList<String>()
                var artists = ArrayList<String>()
                var links = ArrayList<String>()
                for (item in 0..songs.size-1)
                {
                    numbers.add(songs.get(item).number)
                    titles.add(songs.get(item).title)
                    artists.add(songs.get(item).artist)
                    links.add(songs.get(item).link)
                }
                val intent = Intent(this, ListSelect::class.java)
                intent.putStringArrayListExtra("SongsNumbers1", numbers)
                intent.putStringArrayListExtra("SongsTitles1", titles)
                intent.putStringArrayListExtra("SongsArtists1", artists)
                intent.putStringArrayListExtra("SongsLinks1", links)
                startActivity(intent)
            }
            else{
                Toast.makeText(getApplicationContext(), "Downloading songs, try again",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
    override fun onBackPressed() {
        val intent = Intent(this@MainActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}


class MyDownloadCompleteListener : DownloadCompleteListener{
    override fun downloadComplete(result: String) {
        val parseSongs = XMLParser()
        songs = parseSongs.parse(result)
    }

}
private class NetworkReceiver : BroadcastReceiver()
{
    override fun onReceive(context: Context, intent: Intent)
    {   val networkPref = "wifi"
        val connMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connMgr.activeNetworkInfo
        if (networkPref == "wifi" && networkInfo?.type == ConnectivityManager.TYPE_WIFI)
        {

        }
        else if (networkPref == "any" && networkInfo != null)
        {
        }
        else
        {
        }
    }
}
