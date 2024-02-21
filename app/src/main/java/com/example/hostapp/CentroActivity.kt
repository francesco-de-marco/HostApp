package com.example.hostapp

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hostapp.databinding.ActivityCentroBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener

class CentroActivity : AppCompatActivity() {

    private lateinit var listCentro: ListView
    private lateinit var binding: ActivityCentroBinding
    private lateinit var listAdapter: ListAdapter
    private lateinit var listData: ListData
    var dataArrayList = ArrayList<ListData?>()
    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_centro)

        listCentro = findViewById(R.id.listCentro)

        binding = ActivityCentroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val array = intent.getStringArrayExtra("array")

        val citta = array?.get(0)

        val sport = array?.get(1)
        Toast.makeText(this@CentroActivity, "$citta $sport", Toast.LENGTH_SHORT).show()

        val nameList = mutableListOf<String>()
        database = FirebaseDatabase.getInstance()
        reference = database.getReference("location")
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for (childSnapshot in snapshot.children) {

                        val city = childSnapshot.child("citta").getValue(String::class.java)

                        if (city.toString() == citta.toString()) {
                            val sportArray = childSnapshot.child("sport").getValue(object : GenericTypeIndicator<ArrayList<String>>() {})

                            if (sportArray != null && sport in sportArray) {
                                val attivita = childSnapshot.child("attivita").getValue(String::class.java)


                                if (attivita != null) {
                                    nameList.add(attivita)
                                }
                            }
                        }
                    }

                    for (i in nameList.indices) {
                        listData = ListData(
                            nameList[i]
                        )
                        dataArrayList.add(listData)
                    }

                    listAdapter = ListAdapter(this@CentroActivity, dataArrayList)
                    binding.listCentro.adapter = listAdapter
                    binding.listCentro.isClickable = true
                    binding.listCentro.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->

                            val intent = Intent(this@CentroActivity, Main2Activity::class.java)
                            intent.putExtra("sport", nameList[i])
                            startActivity(intent)
                        }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })


    }
}



