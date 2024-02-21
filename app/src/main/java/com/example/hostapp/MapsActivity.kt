package com.example.hostapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.widget.AdapterView
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.hostapp.databinding.ActivityMapsBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException

class MapsActivity : AppCompatActivity(), LocationListener {        //restiutisce la lista dei campi dello sport selezionato in precedenza vicini alla posizione attuale

    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private lateinit var locationManager: LocationManager
    private val locationPermissionCode = 2
    private lateinit var button: Button
    private  lateinit var pos:Location
    private lateinit var listJSON: ListView
    private var locationObtained = false
    private lateinit var binding: ActivityMapsBinding
    private lateinit var listAdapter: ListAdapter
    private lateinit var listData: ListData
    var dataArrayList = ArrayList<ListData?>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        listJSON = findViewById(R.id.listJSON)
        button= findViewById(R.id.getL)
        button.setOnClickListener {
            getLocation()
            Toast.makeText(this, "Attendi qualche secondo!!!", Toast.LENGTH_LONG).show()
        }
    }

    fun prenota(luogo: String){     //aprirà l'activity successiva solo se l'attività è registrata come Gestore
        database = FirebaseDatabase.getInstance()
        reference = database.getReference("users")
        var use= intent.getStringExtra("user2")
        reference.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val checkAtt = reference.orderByChild("attivita").equalTo(luogo)
                checkAtt.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            val intent = Intent(this@MapsActivity, Main2Activity::class.java)
                            intent.putExtra("user3", use )
                            intent.putExtra("luogo",luogo)
                            startActivity(intent)
                        }
                    }
                    override fun onCancelled(databaseError: DatabaseError) {
                    // Handle possible errors.
                    }
                })
            } else{

            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    fun risultati(coordinate: String){          //utilizza una'api che restituisce sotto forma di json i risultati di ricerca di google Maps
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val sport=intent.getStringExtra("name")
        GlobalScope.launch(Dispatchers.IO) { val client = OkHttpClient()
            val apiKey = "27729031ec62dc87d06e5c385d51f3f1800b2ee9c81a21ac6d3894b8f459d2b6"

            val query = "campoda$sport"+"&ll=@$coordinate,15.1z"
            val apiUrl = "https://serpapi.com/search.json?engine=google_maps&q=$query&type=search&key=$apiKey"

            val request = Request.Builder()
                .url(apiUrl)
                .build()

            try {
                val response = client.newCall(request).execute()
                if (!response.isSuccessful) throw IOException("Unexpected code $response")
                    val responseBody = response.body?.string()
                    responseBody?.let { body -> val jsonObj = JSONObject(body)
                        val map = jsonObj.optJSONArray("local_results")
                        val valueList = mutableListOf<JSONObject>()
                        for (i in 0 until 10) {
                            if (map.get(i) is JSONObject) {
                                valueList.add(map.get(i) as JSONObject)
                            }
                        }

                        var namesList = mutableListOf<String>()
                        for (i in 0 until valueList.size){
                            namesList.add(valueList.get(i).get("title").toString())
                        }
                        launch(Dispatchers.Main) {
                            for (i in namesList.indices) {
                                listData = ListData(
                                    namesList[i]
                                )
                                dataArrayList.add(listData)
                            }

                            listAdapter = ListAdapter(this@MapsActivity, dataArrayList)
                            binding.listJSON.adapter = listAdapter
                            binding.listJSON.isClickable = true
                            binding.listJSON.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
                                prenota(namesList[i])

                            }
                        }
                    }
            } catch (e: Exception) {

            }
        }

    }

    private fun getLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
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


        // Verifica esplicitamente se il permesso è stato concesso
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Il permesso non è stato concesso, richiedilo all'utente
            ActivityCompat.requestPermissions(this,arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),locationPermissionCode)
        } else {
            requestLocationUpdates()
        }
    }
    private fun requestLocationUpdates() {
        try {
            // Request location updates only if location hasn't been obtained yet
            if (!locationObtained) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 2f, this)
            }
        } catch (e: SecurityException) {
        }
    }
    override fun onLocationChanged(location: Location) {
        pos=location
        val coordinate = "${pos.latitude},${pos.longitude}"
        Toast.makeText(this, "Ci siamo quasi ... ", Toast.LENGTH_SHORT).show()
        risultati(coordinate)
        locationObtained = true
        locationManager.removeUpdates(this)
    }

    override fun onRequestPermissionsResult(requestCode: Int,permissions: Array<out String>,grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, richiedi gli aggiornamenti della posizione
                requestLocationUpdates()
            } else {
                // Permission denied, gestisci di conseguenza (ad esempio, mostra un messaggio)
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}