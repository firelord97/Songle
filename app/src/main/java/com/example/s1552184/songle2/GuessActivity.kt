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
import android.util.Log
import android.widget.*
import android.widget.ListView
import java.util.*
import kotlin.collections.ArrayList

var wrongguess = 0
class GuessActivity : AppCompatActivity() {
    var list = ArrayList<String>()
    var lyrics = ArrayList<String>()
    var totwords = 0
    var wordguess = 0
    val random = Random()
    var remainingwords = ArrayList<String>()
    private val words = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guess)
        val guessIntent = intent
        val title = guessIntent.getStringExtra("title")
        val link = guessIntent.getStringExtra("link")
        val artist = guessIntent.getStringExtra("artist")
        val thelevel = guessIntent.getIntExtra("thelevel", 0)
        list = guessIntent.getStringArrayListExtra("unlockedwords")
        lyrics = guessIntent.getStringArrayListExtra("lyrics")
        wordguess = guessIntent.getIntExtra("words", 0)
        totwords = guessIntent.getIntExtra("totalwords", 0)
        val lv = findViewById<ListView>(R.id.listview) as ListView
        generateListContent()
        lv.adapter = MyListAdaper(this, R.layout.activity_list_view, words)
        lv.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id -> }
        button_hint.setOnClickListener() {
            var hintline = 0
            val random = Random()
            if (wordguess != totwords) {
                hintline=rand(0, remainingwords.size)
                var word=remainingwords.get(hintline)
                var actualword=word.substring(0, word.indexOf(","))
                var line = word.substring(word.indexOf(",")+1, word.lastIndexOf(","))
                var position = word.substring(word.lastIndexOf(",")+1, word.length )
                Toast.makeText(getApplicationContext(), "Word unlocked: "+actualword,
                        Toast.LENGTH_SHORT).show();
                remainingwords.removeAt(hintline)
                words.add(actualword+"   Line:"+line+" Word:"+position)
                list.add(line+":"+position)
                lv.adapter = MyListAdaper(this, R.layout.activity_list_view, words)
                wordguess+=1
            } else {
                Toast.makeText(getApplicationContext(), "All words unlocked",
                        Toast.LENGTH_SHORT).show();
            }
        }
        button_guesssong.setOnClickListener() {
            val inputtext = editText.text.toString()
            if (inputtext.equals(title, ignoreCase = true)) {
                var score = (100 * (totwords - wordguess) / totwords) * (1 + (thelevel + 1) * 0.2) - (wrongguess + 1) * 10
                Toast.makeText(getApplicationContext(), "Correct Answer!",
                        Toast.LENGTH_SHORT).show();
                val intent = Intent(this@GuessActivity, CorrectAnswer::class.java)
                intent.putExtra("thescore", score.toInt())
                intent.putExtra("thetitle", title)
                intent.putExtra("thelink", link)
                intent.putExtra("theartist", artist)
                startActivity(intent)

            } else {
                Toast.makeText(getApplicationContext(), "Incorrect Answer!",
                        Toast.LENGTH_SHORT).show();
                wrongguess = wrongguess + 1
            }
        }
        give_up.setOnClickListener()
        {
            var score=0
            val intent = Intent(this@GuessActivity, CorrectAnswer::class.java)
            intent.putExtra("thescore", score.toInt())
            intent.putExtra("thetitle", title)
            intent.putExtra("thelink", link)
            intent.putExtra("theartist", artist)
            startActivity(intent)
        }

    }

    private fun generateListContent() {
        for (item in 0..lyrics.size-1) {
            if(lyrics.get(item)!="") {
                var string = lyrics.get(item)
                var spaces = 0
                var point = 0
                for (char in 0..string.length - 1) {
                    if (string[char] == ' ' || char == string.length - 1) {
                        spaces = spaces + 1
                        remainingwords.add(string.substring(point, char+1)+","+(item+1).toString()+","+spaces.toString())
                        point=char+1
                    }
                }
            }
        }
        for (item in list) {
            var line = 0
            var position = 0
            for (char in item) {
                if (char == ':') {
                    line = item.substring(0, item.indexOf(':')).toInt()
                    position = item.substring(item.indexOf(':') + 1, item.length).toInt()
                }
            }
            var string = lyrics.get(line - 1)
            var spaces = 0
            var point = 0
            for (char in 0..string.length - 1) {
                if (string[char] == ' ' || char == string.length - 1) {
                    spaces = spaces + 1
                    if (spaces == position) {
                        words.add(string.substring(point, char + 1) + "   Line:" + line.toString() + " Word:" + position.toString())
                        if(string.substring(point, char+1) in remainingwords)
                        {
                            remainingwords.remove(string.substring(point, char+1)+","+line.toString()+","+position.toString())
                        }
                        spaces += 1
                    } else
                        point = char + 1
                }
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
    fun rand(from: Int, to: Int) : Int {
        return random.nextInt(to - from) + from
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

    override fun onBackPressed() {
        val intent = Intent()
        intent.putStringArrayListExtra("listofwords", list)
        intent.putExtra("wordsguessed", wordguess)
        setResult(0, intent)
        super.onBackPressed()
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
