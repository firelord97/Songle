package com.example.s1552184.songle2

import android.Manifest
import android.Manifest.permission_group.LOCATION
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
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

var wordsguessed = 0
var numberslist =ArrayList<String>()
var titleslist =ArrayList<String>()
var artistslist =ArrayList<String>()
var linkslist =ArrayList<String>()
class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, OnMarkerClickListener {
    var list = mutableListOf<String>()
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
        wordsguessed=0
        val mapIntent = intent
        val songselect = mapIntent.getIntExtra("SelectedSong", 0)
        val levelselect = mapIntent.getIntExtra("LevelSelect", 0)
        numberslist = mapIntent.getStringArrayListExtra("SongsNumbers1")
        titleslist = mapIntent.getStringArrayListExtra("SongsTitles1")
        artistslist = mapIntent.getStringArrayListExtra("SongsArtists1")
        linkslist = mapIntent.getStringArrayListExtra("SongsLinks1")
        val correctanswer: TextView = findViewById(R.id.correctanswers)
        correctanswer.setText(wordsguessed.toString()+"/371");
        guess.setOnClickListener() {
            val intent = Intent(this, GuessActivity::class.java)
            intent.putExtra("title", titleslist.get(songselect))
            intent.putExtra("artist", artistslist.get(songselect))
            intent.putExtra("link", linkslist.get(songselect))
            intent.putExtra("thelevel", levelselect)
            intent.putExtra("words", wordsguessed)
            startActivity(intent)

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
        var layer = KmlLayer(mMap, R.raw.map5, applicationContext)
        layer.addLayerToMap()
        mMap.uiSettings.isMyLocationButtonEnabled = true
        mMap.setOnMarkerClickListener(this)
    }


    override fun onMarkerClick(marker: Marker): Boolean {
        if(marker.title in list)
        {}
        else {
            list.add(marker.title)
            Toast.makeText(getApplicationContext(), "Word Unlocked: baby",
                    Toast.LENGTH_LONG).show();
            marker.setIcon((BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)))
            wordsguessed = wordsguessed+1
            val correctanswer: TextView = findViewById(R.id.correctanswers)
            correctanswer.setText(wordsguessed.toString()+"/371");
        }
        return false
    }
}




