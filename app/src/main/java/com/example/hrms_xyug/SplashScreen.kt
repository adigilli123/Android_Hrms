package com.xyug.hrms

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.hrms_xyug.HomeActivityViewController
import com.example.hrms_xyug.MainActivity
import com.example.hrms_xyug.R
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResult
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.tasks.OnSuccessListener

class SplashScreen : AppCompatActivity() {
    private lateinit var handler: Handler
    private var googleApiClient: GoogleApiClient? = null
    private val REQUEST = 112
    private val REQUEST_LOCATION = 199
    private val mContext: Context = this
    private var mToken = ""
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        init()
    }

    private fun init() {
        handler = Handler()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (isConnected()) {
            checkRuntimePermissions()
        } else {
            Toast.makeText(applicationContext, "No Internet Connection", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkIfUserHavingData() {
        handler.postDelayed({
            val accessToken = AccountUtils.getAccessToken(applicationContext)
            if (accessToken.isEmpty()) {
                startActivity(Intent(this@SplashScreen, MainActivity::class.java))
            } else {
                getLocationAndSave()
            }
            finish()
        }, 2000)
    }

//    private fun getLocationAndSave() {
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) == PackageManager.PERMISSION_GRANTED
//        ) {
//            fusedLocationClient.lastLocation
//                .addOnSuccessListener(this, OnSuccessListener<Location> { location ->
//                    if (location != null) {
//                        val latitude = location.latitude
//                        val longitude = location.longitude
//
//
//                        Log.d("LatitudeLocation", "Latitude: $latitude, Longitude: $longitude")
//
//                        // Save latitude and longitude in AccountUtils
//                        AccountUtils.setLatitude(applicationContext, latitude)
//                        AccountUtils.setLongitude(applicationContext, longitude)
//
//                        Log.d("LatitudeLocation", "Latitude and Longitude saved in AccountUtils")
//
//                        // Start the next activity
//                        startActivity(Intent(this@SplashScreen, HomeActivityViewController::class.java))
//                    } else {
//                        Log.e("LatitudeLocation", "Location is null, unable to get location.")
//                        Toast.makeText(this, "Unable to get location", Toast.LENGTH_SHORT).show()
//                    }
//                })
//        }
//    }

   private fun getLocationAndSave() {
     if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
         fusedLocationClient.lastLocation
            .addOnSuccessListener(this, OnSuccessListener<Location> { location ->
                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude

                    Log.d("LatitudeLocation", "Latitude: $latitude, Longitude: $longitude")

                    AccountUtils.setLatitude(applicationContext, latitude)
                    AccountUtils.setLongitude(applicationContext, longitude)

                    Log.d("LatitudeLocation", "Latitude and Longitude saved in AccountUtils")
                } else {
                    Log.e("LatitudeLocation", "Location is null, unable to get location now.")
                }

                startActivity(Intent(this@SplashScreen, HomeActivityViewController::class.java))
                finish()
            })
            .addOnFailureListener {
                Log.e("LocationError", "Failed to get location.", it)
                startActivity(Intent(this@SplashScreen, HomeActivityViewController::class.java))
                finish()
            }
    } else {
        startActivity(Intent(this@SplashScreen, HomeActivityViewController::class.java))
        finish()
    }
  }

    private fun checkRuntimePermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val PERMISSIONS = arrayOf(
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE)

            if (!hasPermissions(mContext, *PERMISSIONS)) {
                ActivityCompat.requestPermissions(mContext as Activity, PERMISSIONS, REQUEST)
            } else {
                checker()
            }
        } else {
            checker()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checker()
            } else {
                Toast.makeText(mContext, "Permissions Denied", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun hasPermissions(context: Context, vararg permissions: String): Boolean {
        for (permission in permissions) {
            if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    private fun checker() {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            checkIfUserHavingData()
        } else {
            enableLoc()
        }
    }

    private fun enableLoc() {
        if (googleApiClient == null) {
            googleApiClient = GoogleApiClient.Builder(this).addApi(LocationServices.API).addConnectionCallbacks(object : GoogleApiClient.ConnectionCallbacks {
                    override fun onConnected(bundle: Bundle?) {
                    }

                    override fun onConnectionSuspended(i: Int) {
                        googleApiClient?.connect()
                    }
                })
                .addOnConnectionFailedListener { connectionResult ->
                    Log.d("Location error", "Location error ${connectionResult.errorCode}")
                }.build()
            googleApiClient?.connect()

            val locationRequest = LocationRequest.create().apply {
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                interval = (30 * 1000).toLong()
                fastestInterval = (5 * 1000).toLong()
            }

            val builder = LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
            builder.setAlwaysShow(true)

            val result: PendingResult<LocationSettingsResult> =
                LocationServices.SettingsApi.checkLocationSettings(googleApiClient!!, builder.build())
            result.setResultCallback { result ->
                val status: Status = result.status
                when (status.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        try {
                            status.startResolutionForResult(this@SplashScreen, REQUEST_LOCATION)
                        } catch (e: IntentSender.SendIntentException) {
                            e.printStackTrace()
                        }
                    }
                    LocationSettingsStatusCodes.SUCCESS -> {
                        checkIfUserHavingData()
                    }
                }
            }
        }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        when (requestCode) {
//            REQUEST_LOCATION -> when (resultCode) {
//                Activity.RESULT_OK -> {
//                    checkIfUserHavingData()
//                }
//                Activity.RESULT_CANCELED -> {
//                    Toast.makeText(this, "Location is required to continue", Toast.LENGTH_SHORT).show()
//                    finish()
//                }
//            }
//        }
//    }
     
   override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    when (requestCode) {
        REQUEST_LOCATION -> when (resultCode) {
            Activity.RESULT_OK -> {
                // Try to get location again after enabling it
                getLocationAndSave()
            }
            Activity.RESULT_CANCELED -> {
                // If the user cancels enabling location, proceed to the next activity
                Toast.makeText(this, "Location is not enabled, continuing without location.", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@SplashScreen, HomeActivityViewController::class.java))
                finish()
            }
        }
    }
}

    private fun isConnected(): Boolean {
        try {
            val cm = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val nInfo = cm.activeNetworkInfo
            return nInfo != null && nInfo.isAvailable && nInfo.isConnected
        } catch (e: Exception) {
            Log.e("Connectivity Exception", e.message!!)
        }
        return false
    }
}
