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
import kotlinx.android.synthetic.main.activity_guess.*
import android.R.attr.duration
import android.R.attr.editTextBackground
import android.widget.*
import android.widget.ListView

var wrongguess = 0
class GuessActivity : AppCompatActivity() {

    private val words = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guess)
        val lv = findViewById<ListView>(R.id.listview) as ListView
        generateListContent()
        lv.adapter = MyListAdaper(this, R.layout.activity_list_view, words)
        lv.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id -> Toast.makeText(this@GuessActivity, "List item was clicked at " + position, Toast.LENGTH_SHORT).show() }
        val guessIntent = intent
        val title = guessIntent.getStringExtra("title")
        val link = guessIntent.getStringExtra("link")
        val artist = guessIntent.getStringExtra("artist")
        val thelevel = guessIntent.getIntExtra("thelevel", 0)
        var wordguess = guessIntent.getIntExtra("words", 0)
        button_hint.setOnClickListener(){
            Toast.makeText(getApplicationContext(), "New Word Unlocked: matters",
                    Toast.LENGTH_SHORT).show();
            wordguess+1
        }
        button_guesssong.setOnClickListener(){
            val inputtext= editText.text.toString()
            if(inputtext.equals(title, ignoreCase = true)) {
                var score = (100*(371-wordguess)/371)*(1+(thelevel+1)*0.1)- (wrongguess+1)*10
                Toast.makeText(getApplicationContext(), "Correct Answer!",
                        Toast.LENGTH_SHORT).show();
                val intent = Intent(this@GuessActivity, CorrectAnswer::class.java)
                intent.putExtra("thescore", score.toInt())
                intent.putExtra("thetitle", title)
                intent.putExtra("thelink", link)
                intent.putExtra("theartist", artist)
                startActivity(intent)

            }
            else
            {
                Toast.makeText(getApplicationContext(), "Incorrect Answer!",
                        Toast.LENGTH_SHORT).show();
                wrongguess=wrongguess+1
            }
        }

    }

    private fun generateListContent() {
        words.add("baby"+"     "+"Line:60  Word:8")
        words.add("matters"+"     "+ "Line:68  Word:3")
        for (i in 1..25) {
            words.add("Word "+i)
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

    inner class MyListAdaper constructor(context: Context, private val layout: Int, private val mObjects: List<String>) : ArrayAdapter<String>(context, layout, mObjects) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var convertView = convertView
            var mainViewholder: ViewHolder? = null
            if (convertView == null) {
                val inflater = LayoutInflater.from(context)
                convertView = inflater.inflate(layout, parent, false)
                val viewHolder = ViewHolder()
                viewHolder.title = convertView.findViewById<TextView>(R.id.list_view_text) as TextView
                convertView.tag = viewHolder
            }
            mainViewholder = convertView!!.tag as ViewHolder
            mainViewholder.title!!.text = getItem(position)

            return convertView
        }
    }

    inner class ViewHolder {

        internal var title: TextView? = null

    }
}
