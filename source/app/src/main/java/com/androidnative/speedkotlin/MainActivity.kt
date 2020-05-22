package com.androidnative.speedkotlin

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.androidnative.speedkotlin.business.CAverage
import com.androidnative.speedkotlin.business.CLocation
import com.androidnative.speedkotlin.utils.MAIN_FRAGMENT_LAYOUT
import com.androidnative.speedkotlin.utils.MAIN_FRAGMENT_TAG
import com.androidnative.speedkotlin.utils.RC_FINE_LOCATION
import com.androidnative.speedkotlin.utils.STOP_FRAGMENT_TAG
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private var locationManager: LocationManager? = null
    private var mSpeedResult: Short = 0
    private lateinit var mJob: Job
    private val mCalculator: CAverage by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null){
            supportFragmentManager.beginTransaction()
                .add(MAIN_FRAGMENT_LAYOUT, RunFragment.newInstance(), MAIN_FRAGMENT_TAG)
                .commit()
        }

        CoroutineScope(Dispatchers.Main).launch {
            mJob = doJob()
        }

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
            mSpeedResult = myLocation.speed.toShort()
        }

        override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {}
        override fun onProviderEnabled(p0: String?) {}
        override fun onProviderDisabled(p0: String?) {}
    }

    override fun onDestroy() {
        super.onDestroy()
        mJob.cancel()
    }

    /**
     *  Coroutine : Fragment choice
     */

    private fun doJob() : Job = CoroutineScope(Dispatchers.Main).launch {
        while (true) {
            delay(1000)
            val fragment: Fragment? = supportFragmentManager.findFragmentByTag(MAIN_FRAGMENT_TAG)
            if (mSpeedResult.toInt() != 0){
                if (fragment != null){
                    mCalculator.value = mSpeedResult
                    (fragment as RunFragment).updateUI("$mSpeedResult", mCalculator.getResult())
                } else {
                    supportFragmentManager.beginTransaction().replace(MAIN_FRAGMENT_LAYOUT, RunFragment.newInstance(), MAIN_FRAGMENT_TAG).commit()
                }
            } else {
                fragment?.let {
                    supportFragmentManager.beginTransaction().replace(MAIN_FRAGMENT_LAYOUT, StopFragment.newInstance(mCalculator.average.toShort()), STOP_FRAGMENT_TAG).commit()
                }
            }
        }
    }
}


