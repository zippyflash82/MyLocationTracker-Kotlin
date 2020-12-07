package com.example.mylocationtracker

import android.app.Notification.EXTRA_TITLE
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest.Builder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity(),LocationListener{
    private lateinit var locationManager: LocationManager
    private val locationPermissionCode = 2
    var isGpsEnabled = false
    var workManager: WorkManager?=null

    var currentLocation: Location ?=null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        isGpsEnabled =locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        //val uploadWorkRequest: WorkRequest =OneTimeWorkRequest.Builder(BackgroundLocationUpload::class.java).build()
     //   val periodicWorkRequest = PeriodicWorkRequestBuilder<BackgroundLocationUpload>(15,TimeUnit.MINUTES,15,TimeUnit.MINUTES) as PeriodicWorkRequest


     //      var data1 = workDataOf("lat" to currentLocation!!.latitude.toString())
     //      var data2 = workDataOf("long" to currentLocation!!.latitude.toString())



        val saveRequest =
            PeriodicWorkRequestBuilder<BackgroundLocationUpload>(15, TimeUnit.MINUTES)
               // .setInputData(data1)
               // .setInputData(data2)
                .build()

        btn_get_location.setOnClickListener {
            getLocation()
            workManager = WorkManager.getInstance(this)
            workManager!!.enqueueUniquePeriodicWork("", ExistingPeriodicWorkPolicy.KEEP, saveRequest)

        }
    }




    private fun getLocation() {
        if ((ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionCode)
        }
        if (isGpsEnabled)

        {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 200, 1f, this)
        }

        else {
            Toast.makeText(this, "Please Enable GPS", Toast.LENGTH_LONG).show()
        }
    }


    override fun onLocationChanged(location: Location) {
          Toast.makeText(this, "Current Lat: ${location!!.latitude}, Current Long: ${location!!.longitude}", Toast.LENGTH_LONG).show()

          currentLocation = location
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onProviderEnabled(provider: String) {
        super.onProviderEnabled(provider)
        isGpsEnabled = true
    }

    override fun onProviderDisabled(provider: String) {
        super.onProviderDisabled(provider)
        isGpsEnabled = false
    }







}