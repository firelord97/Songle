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
import android.content.Intent.getIntent





class LevelSelect : AppCompatActivity() {

    private val maps = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_level_select)
        val lv = findViewById<ListView>(R.id.listview) as ListView
        generateListContent()
        lv.adapter = MyListAdaper(this, R.layout.list_item, maps)
        lv.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id -> Toast.makeText(this@LevelSelect, "List item was clicked at " + position, Toast.LENGTH_SHORT).show() }
    }

    private fun generateListContent() {
        maps.add("Justin Bieber (Very Easy)")
        maps.add("Prince (Easy)")
        maps.add("David Bowie (Moderate)")
        maps.add("Michael Jackson (Hard)")
        maps.add("Elvis Presley (Very Hard)")
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

    inner class MyListAdaper constructor(context: Context, private val layout: Int, private val mObjects: List<String>) : ArrayAdapter<String>(context, layout, mObjects) {

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
                val mIntent = intent
                val selectsong = mIntent.getIntExtra("SongLevel", 0)
                val intent = Intent(this@LevelSelect, MapsActivity::class.java)
                intent.putExtra("SelectedSong", selectsong)
                intent.putExtra("LevelSelect", position)
                startActivity(intent)}
            mainViewholder.title!!.text = getItem(position)

            return convertView
        }
    }

    inner class ViewHolder {

        internal var title: TextView? = null
        internal var button: Button? = null
    }
}
