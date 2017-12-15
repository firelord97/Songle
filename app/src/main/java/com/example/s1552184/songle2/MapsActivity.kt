package com.example.s1552184.songle2

import android.Manifest
import android.Manifest.permission_group.LOCATION
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.ContextCompat.checkSelfPermission
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.example.s1552184.songle2.R.raw.map5
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_maps.*
import com.google.maps.android.kml.KmlLayer
import com.google.maps.android.kml.KmlPlacemark
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.model.*
import com.google.maps.android.kml.KmlContainer
import java.io.File
import java.io.InputStream

var wordsguessed = 0 //numbers of words guessed by user
//details of songs from songlist
var numberslist =ArrayList<String>()
var titleslist =ArrayList<String>()
var artistslist =ArrayList<String>()
var linkslist =ArrayList<String>()
var remainingwords =ArrayList<String>() //list of remainingwords
var songselect: Int = 0 //song selected by user
var levelselect: Int = 0 //level selected by user
var mapname="" //stores filename of map
var wordcount= 0 //number of words in lyrics
var hintguess=0 //number of hints used by user
var wrongguess=0 //number of incorrect guesses
var hasgoneback=0 //1 if user returns to this activity from guessactivity
class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, OnMarkerClickListener {
    var list = ArrayList<String>() //list of word tags(e.g. "60:8")
    var lyrics = ArrayList<String>() //list of song lyrics where each element is a line
    private lateinit var mMap: GoogleMap
    private lateinit var mGoogleApiClient: GoogleApiClient
    val permissionsRequestAccessFineLocation = 1
    var mLocationPermissionGranted = false
    // getLastLocation can return null, so we need the type ”Location?”
    private var mLastLocation: Location? = null
    val tag = "MapsActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        // Create an instance of GoogleAPIClient.
        mGoogleApiClient = GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()
        //reset value each time new map opened
        //receive intent values
        wordsguessed = 0
        val mapIntent = intent
        songselect = mapIntent.getIntExtra("SelectedSong", 0)
        levelselect = mapIntent.getIntExtra("LevelSelect", 0)
        numberslist = mapIntent.getStringArrayListExtra("SongsNumbers1")
        titleslist = mapIntent.getStringArrayListExtra("SongsTitles1")
        artistslist = mapIntent.getStringArrayListExtra("SongsArtists1")
        linkslist = mapIntent.getStringArrayListExtra("SongsLinks1")
        //callers to download map and lyrics
        val caller = MapDownloadListener()
        val lyriccaller = LyricDownloadListener()
        val lyricdownloader = DownloadXmlTask(lyriccaller)
        val downloader = DownloadXmlTask(caller)
        if (songselect < 9) {//url different for less than 10, 01 instead of 1
            downloader.execute("http://www.inf.ed.ac.uk/teaching/courses/cslp/data/songs/0" + (songselect + 1).toString() + "/map" + (5 - levelselect).toString() + ".kml")
            lyricdownloader.execute("http://www.inf.ed.ac.uk/teaching/courses/cslp/data/songs/0" + (songselect + 1).toString() + "/lyrics.txt")
        } else {
            downloader.execute("http://www.inf.ed.ac.uk/teaching/courses/cslp/data/songs/" + (songselect + 1).toString() + "/map" + (5 - levelselect).toString() + ".kml")
            lyricdownloader.execute("http://www.inf.ed.ac.uk/teaching/courses/cslp/data/songs/" + (songselect + 1).toString() + "/lyrics.txt")
        }

        guess.setOnClickListener() {//takes to guess song activity with required values in intent
            val intent = Intent(this, GuessActivity::class.java)
            intent.putExtra("title", titleslist.get(songselect))
            intent.putExtra("artist", artistslist.get(songselect))
            intent.putExtra("link", linkslist.get(songselect))
            intent.putExtra("thelevel", levelselect)
            intent.putExtra("words", wordsguessed)
            intent.putStringArrayListExtra("unlockedwords", list)
            intent.putStringArrayListExtra("lyrics", lyrics)
            intent.putExtra("totalwords", wordcount)
            intent.putExtra("hintused", hintguess)
            intent.putExtra("wrongguess", wrongguess)
            intent.putExtra("hasgoneback", hasgoneback)
            intent.putStringArrayListExtra("remainingwords", remainingwords)
            startActivityForResult(intent, 0)


        }
    }

    override fun onStart() {
        super.onStart()
        mGoogleApiClient.connect()
    }

    override fun onStop() {
        super.onStop()
        if (mGoogleApiClient.isConnected) {
            mGoogleApiClient.disconnect()
        }
    }

    fun createLocationRequest() {
// Set the parameters for the location request
        val mLocationRequest = LocationRequest()
        mLocationRequest.interval = 5000 // preferably every 5 seconds
        mLocationRequest.fastestInterval = 1000 // at most every second
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
// Can we access the user’s current location?
        val
                permissionCheck = checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this)
        }
    }

    override fun onConnected(connectionHint: Bundle?) {
        try {
            createLocationRequest()
        } catch
        (ise: IllegalStateException) {
            println("[$tag] [onConnected] IllegalStateException thrown")
        }
// Can we access the user’s current location?
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            val api = LocationServices.FusedLocationApi
            mLastLocation = api.getLastLocation(mGoogleApiClient)
            if (mLastLocation == null) {
                println("[$tag] Warning: mLastLocation is null")
            }
        } else {
            ActivityCompat.requestPermissions(
                    this
                    ,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    permissionsRequestAccessFineLocation)
        }
    }

    override fun
            onLocationChanged(current: Location?) {
        if (current == null) {
            println("[$tag] [onLocationChanged] Location unknown")
        } else {
            println("""[$tag] [onLocationChanged] Lat/long now(${current.latitude},${current.longitude})""")
        }
    }

    override fun onConnectionSuspended(flag: Int) {
        println(">>>>onConnectionSuspended")

    }

    override fun onConnectionFailed(result: ConnectionResult) {
        println(">>>>onConnectionFailed")
    }

    /**
     *
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isMyLocationButtonEnabled = true
        try {
            mMap.isMyLocationEnabled = true
        } catch (se: SecurityException) {
            println("Exception onMapReady")
        }
        // Add a marker in Sydney and move the camera
        val Edinburgh = LatLng(55.944159, -3.187574)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Edinburgh, 15f))
    }

    //very important method, lots of values change when marker is clicked
    override fun onMarkerClick(marker: Marker): Boolean {
        //set values of marker location to location object
        val temp = Location(LocationManager.GPS_PROVIDER)
        temp.setLatitude(marker.position.latitude)
        temp.setLongitude(marker.position.longitude)

        if (mLastLocation!!.distanceTo(temp) < 25) {//check if user is within 25 metres of marker, based on last location
            if (marker.title in list) {//check if word is already unlocked
                Toast.makeText(getApplicationContext(), "Word already unlocked",
                        Toast.LENGTH_SHORT).show();
                marker.setIcon((BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)))
            } else {
                list.add(marker.title)
                marker.setIcon((BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))) //change marker colour
                wordsguessed = wordsguessed + 1
                val correctanswer: TextView = findViewById(R.id.correctanswers)
                correctanswer.setText(wordsguessed.toString() + "/" + wordcount.toString()) //reset words unlocked count
                // following part uses the tag to find the exact word from the lyrics
                var item=marker.title.toString()
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
                    for (char in 0..string.length-1) {
                        if (string[char] == ' ' || char==string.length-1) {
                            spaces = spaces + 1
                            if (spaces == position) {
                                Toast.makeText(getApplicationContext(), "New word unlocked: "+string.substring(point, char+1), //display word
                                        Toast.LENGTH_SHORT).show();
                                spaces += 1
                            } else
                                point = char + 1
                        }
                    }

            }
        } else {//display user is too far to unlock and how far away they are
            Toast.makeText(getApplicationContext(), "You are too far away: "+Math.round(mLastLocation!!.distanceTo(temp)).toString()+" metres",
                    Toast.LENGTH_SHORT).show();
        }
        return false
    }

    fun showMap() { //creating a separate function that adds the markers to the map ensures we can wait till the download of map and lyrics is complete
        val inputStream: InputStream = openFileInput(mapname) //mapname is the map file
        val layer = KmlLayer(mMap, inputStream, applicationContext)
        layer.addLayerToMap()
        val inputAsString = openFileInput(mapname).bufferedReader().use { it.readText() }
        wordcount = inputAsString.split("<name>").size - 1
        val correctanswer: TextView = findViewById(R.id.correctanswers)
        correctanswer.setText(wordsguessed.toString() + "/" + wordcount.toString()) //we now how have totalwords on the map from the xml file
        mMap.uiSettings.isMyLocationButtonEnabled = true
        mMap.setOnMarkerClickListener(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {//when user returns to map activity from guess, the variables are updated
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==0)
        {
            list=data.getStringArrayListExtra("listofwords")
            wordsguessed = data.getIntExtra("wordsguessed", 0)
            hintguess= data.getIntExtra("hintused", 0)
            wrongguess=data.getIntExtra("wrongguess", 0)
            hasgoneback=data.getIntExtra("hasgoneback",0)
            hasgoneback=1 //change since user has come back
            remainingwords=data.getStringArrayListExtra("remainingwords")
            val correctanswer: TextView = findViewById(R.id.correctanswers)
            correctanswer.setText(wordsguessed.toString() + "/" + wordcount.toString())

        }
    }

    //Download Map
    inner class MapDownloadListener() : DownloadCompleteListener {
        override fun downloadComplete(result: String) {
            mapname = "song" + (songselect + 1).toString() + "map" + (levelselect + 1).toString()
            val fos = openFileOutput(mapname, Context.MODE_PRIVATE)
            fos.write(result.toByteArray())
            fos.close()
        }
    }
    //Download Lyrics
    inner class LyricDownloadListener() : DownloadCompleteListener {
        override fun downloadComplete(result: String) {
            var count = 0
            for (item in 0..result.length - 1) {
                if (result[item] == '\n') {
                            var tempstring = result.substring(count, item)
                            val re = Regex("[^-A-Za-z0-9\n' ]")
                            //Removes punctuators
                            tempstring = re.replace(tempstring, "") // works
                            lyrics.add(tempstring)
                            count = item + 1
                        }
                }
                showMap()
            //Only show map once map and lyrics are downloaded
            }
        }
    }





