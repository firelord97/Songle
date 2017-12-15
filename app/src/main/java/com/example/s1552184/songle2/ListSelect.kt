package com.example.s1552184.songle2

import android.R.menu
import android.content.Context
import android.content.Intent
import android.support.v7.app.*
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast



class ListSelect : AppCompatActivity() {
    //Receive details of song list and store them in variables
    var numberslist =ArrayList<String>()
    var titleslist =ArrayList<String>()
    var artistslist =ArrayList<String>()
    var linkslist =ArrayList<String>()
    private val data = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_select)
        //receive detials of songlist
        val mIntent = intent
        numberslist = mIntent.getStringArrayListExtra("SongsNumbers1")
        titleslist = mIntent.getStringArrayListExtra("SongsTitles1")
        artistslist = mIntent.getStringArrayListExtra("SongsArtists1")
        linkslist = mIntent.getStringArrayListExtra("SongsLinks1")
        val lv = findViewById<ListView>(R.id.listview) as ListView
        //produces list of items the adapter will display
        generateListContent()
        //display list of song choices
        lv.adapter = MyListAdapter(this, R.layout.list_item, data)
        lv.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id -> Toast.makeText(this@ListSelect, "List item was clicked at " + position, Toast.LENGTH_SHORT).show() }
    }
    //list to be displayed
    private fun generateListContent() {
        for (i in 0..numberslist.size-1) {
            data.add("Song " + (i+1))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)

    }

     inner class MyListAdapter constructor(context: Context, private val layout: Int, private val mObjects: List<String>) : ArrayAdapter<String>(context, layout, mObjects) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var convertView = convertView
            var mainViewholder: ViewHolder? = null
            if (convertView == null) {
                val inflater = LayoutInflater.from(context)
                convertView = inflater.inflate(layout, parent, false)
                val viewHolder = ViewHolder()
                viewHolder.title = convertView.findViewById<TextView>(R.id.list_item_text) as TextView
                viewHolder.button = convertView.findViewById<Button>(R.id.list_item_btn) as Button
                convertView.tag = viewHolder
            }
            mainViewholder = convertView!!.tag as ViewHolder
            mainViewholder.button!!.setOnClickListener {
                val intent = Intent(this@ListSelect, LevelSelect::class.java)
                intent.putExtra("SongLevel", position)
                intent.putStringArrayListExtra("SongsNumbers1", numberslist)
                intent.putStringArrayListExtra("SongsTitles1", titleslist)
                intent.putStringArrayListExtra("SongsArtists1", artistslist)
                intent.putStringArrayListExtra("SongsLinks1", linkslist)
                startActivity(intent)
                }
            mainViewholder.title!!.text = getItem(position)

            return convertView
        }
    }

    inner class ViewHolder {

        internal var title: TextView? = null
        internal var button: Button? = null
    }
}
