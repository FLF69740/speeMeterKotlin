package com.androidnative.speedkotlin

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.androidnative.speedkotlin.business.CLocation
import com.androidnative.speedkotlin.utils.RC_FINE_LOCATION
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var locationManager: LocationManager? = null

    private lateinit var mSpeedCounter: TextView

    private var mSpeedResult: Short = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        doLocate()
    }

    /**
     *  LOCATION MANAGER
     */

    private fun doLocate(){
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager?

        locationManager?.let {
            try {
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                    requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), RC_FINE_LOCATION)
                    return
                }
                it.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListener)
            } catch (e: SecurityException) {
                Toast.makeText(this, "no location available", Toast.LENGTH_SHORT).show()
            }
        }

        mSpeedCounter = counter_speed

    }

    /**
     *  PERMISSION
     */

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) = if (requestCode == RC_FINE_LOCATION && grantResults[0] == PackageManager.PERMISSION_GRANTED){
        doLocate()
    } else finish()


    /**
     *  LOCATION LISTENER METHODS
     */

    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            val myLocation = CLocation(location)
            mSpeedCounter.text = "${myLocation.speed.toShort()}"
        }

        override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {}
        override fun onProviderEnabled(p0: String?) {}
        override fun onProviderDisabled(p0: String?) {}
    }
}


