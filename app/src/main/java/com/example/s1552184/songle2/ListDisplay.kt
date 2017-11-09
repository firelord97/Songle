package com.example.s1552184.songle2

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.SimpleAdapter



class ListDisplay : AppCompatActivity() {
    val values = arrayOf("Song 01", "Song 02", "Song 03", "Song 04", "Song 05", "Song 06", "Song 07", "Song 08", "Song 09", "Song 10", "Song 11", "Song 12", "Song 13", "Song 14", "Song 15", "Song 16", "Song 17", "Song 18")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_display)
        val adapter1 = ArrayAdapter(this, android.R.layout.simple_list_item_1, values)
        val listView = findViewById<ListView>(R.id.Songslist) as ListView
        listView.setAdapter(adapter1)
    }
}
