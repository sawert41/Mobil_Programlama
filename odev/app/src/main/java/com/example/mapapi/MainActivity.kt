package com.example.mapapi

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btnCurrentLocation).setOnClickListener {
            startActivity(Intent(this, CurrentLocationActivity::class.java))
        }

        findViewById<Button>(R.id.btnSetLocation).setOnClickListener {
            startActivity(Intent(this, SetLocationActivity::class.java))
        }
    }
}

class CurrentLocationActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var map: GoogleMap
    private val istanbul = LatLng(41.015137, 28.979530)

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) showCurrentLocation() else showDefaultLocation()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_current_location)
        findViewById<Button>(R.id.btnBack).setOnClickListener { finish() }

        val fragment = supportFragmentManager
            .findFragmentById(R.id.mapFragment) as SupportMapFragment
        fragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        if (hasLocationPermission()) {
            showCurrentLocation()
        } else {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun showCurrentLocation() {
        if (!hasLocationPermission()) return
        map.isMyLocationEnabled = true

        LocationServices.getFusedLocationProviderClient(this).lastLocation
            .addOnSuccessListener { location ->
                if (location == null) {
                    showDefaultLocation()
                    Toast.makeText(this, "Konum bulunamadi", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }

                val current = LatLng(location.latitude, location.longitude)
                map.clear()
                map.addMarker(MarkerOptions().position(current).title("Mevcut Konumum"))
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(current, 15f))
            }
            .addOnFailureListener { showDefaultLocation() }
    }

    private fun showDefaultLocation() {
        map.clear()
        map.addMarker(MarkerOptions().position(istanbul).title("Istanbul"))
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(istanbul, 11f))
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
}

class SetLocationActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var map: GoogleMap
    private lateinit var selectedLocationText: TextView
    private val startPoint = LatLng(41.015137, 28.979530)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_location)
        findViewById<Button>(R.id.btnBack).setOnClickListener { finish() }
        selectedLocationText = findViewById(R.id.txtSelectedLocation)

        val fragment = supportFragmentManager
            .findFragmentById(R.id.mapFragment) as SupportMapFragment
        fragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        setMarker(startPoint)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(startPoint, 11f))

        map.setOnMapClickListener { point ->
            setMarker(point)
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 14f))
        }
    }

    private fun setMarker(point: LatLng) {
        map.clear()
        map.addMarker(MarkerOptions().position(point).title("Secilen Konum"))
        selectedLocationText.text = "Secilen konum: %.5f, %.5f".format(point.latitude, point.longitude)
    }
}
