package com.example.s1552184.songle2

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_correct_answer.*

class CorrectAnswer : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_correct_answer)
        val score = intent.getIntExtra("thescore", 0)
        val title = intent.getStringExtra("thetitle")
        val artist = intent.getStringExtra("theartist")
        val link = intent.getStringExtra("thelink")
        songinfo.setText("Score : "+score.toString()+ "\n Artist: "+artist+"\nSong: "+title)
        mainmenu.setOnClickListener(){
            val intent = Intent(this@CorrectAnswer, MainActivity::class.java)
            startActivity(intent)
        }
        youtube.setOnClickListener(){
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
            Log.i("Video", "Video Playing....")
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this@CorrectAnswer, MainActivity::class.java)
        startActivity(intent)
    }
}
