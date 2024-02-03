
package com.example.hostapp


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener
import com.google.android.gms.maps.GoogleMap.OnMyLocationClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment

internal class MapsActivity : AppCompatActivity(),
    OnMyLocationButtonClickListener,
    OnMyLocationClickListener,
    OnMapReadyCallback {

    private val FINE_PERMISSION_CODE = 1
    private lateinit var currentLocation: Location

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        getLastLocation()
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun getLastLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                FINE_PERMISSION_CODE
            )
            return
        }
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        if (!isGpsEnabled) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("GPS disattivato")
            builder.setMessage("Per utilizzare questa funzione, è necessario attivare il GPS. Vuoi attivarlo ora?")
            builder.setPositiveButton("Sì") { dialog, which ->
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                onDestroy()
            }
            builder.setNegativeButton("No") { dialog, which ->
                dialog.dismiss()
            }
            builder.show()
            return
        }
    }

    override fun onMapReady(map: GoogleMap) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        map.isMyLocationEnabled = true
        map.setOnMyLocationButtonClickListener(this)
        map.setOnMyLocationClickListener(this)
    }

    override fun onMyLocationClick(location: Location) {
            Toast.makeText(this, "Current location:\n$location", Toast.LENGTH_LONG)
                .show()
        }

        override fun onMyLocationButtonClick(): Boolean {
            Toast.makeText(this, "Sei qui", Toast.LENGTH_SHORT)
                .show()
            return false
        }

        override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
        ) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)

            if (requestCode == FINE_PERMISSION_CODE)
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLastLocation()
                } else {
                    Toast.makeText(
                        this,
                        "Permessi di localizzazione negati, per favore accetta",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
}
      