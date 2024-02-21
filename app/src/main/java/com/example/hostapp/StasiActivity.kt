package com.example.hostapp

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class StasiActivity : AppCompatActivity() { //Home page dello Sportivo dove è possibile visualizzare le prenotazioni effettuate

    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private lateinit var button: Button
    private lateinit var listView: ListView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stasi)

        listView = findViewById(R.id.listPre)
        button = findViewById(R.id.buttonPren)

        var us = intent.getStringExtra("user")

        var nomeList = mutableListOf<String>()

        val database = FirebaseDatabase.getInstance()
        val reference = database.getReference("prenotazioni")


        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Per ogni "primaChiave" nel nodo "prenotazioni"
                for (primaChiaveSnapshot in dataSnapshot.children) {
                    // Per ogni "secondaChiave" sotto la "primaChiave"
                    for (secondaChiaveSnapshot in primaChiaveSnapshot.children) {
                        // Per ogni "terzaChiave" sotto la "secondaChiave"
                        for (terzaChiaveSnapshot in secondaChiaveSnapshot.children) {
                            // Ottieni il valore di "nome" per la "terzaChiave"
                            val nome = terzaChiaveSnapshot.child("nome").getValue(String::class.java)

                            if (nome == us) {
                                nomeList.add("${primaChiaveSnapshot.key!!} giorno: ${secondaChiaveSnapshot.key!!} ore: ${terzaChiaveSnapshot.key!!}")
                            }
                        }
                    }
                }
                if(nomeList.size==0)
                    nomeList.add("Nessuna Prenotazione al momento")

                val arrayAdapter = ArrayAdapter(this@StasiActivity, R.layout.lista, R.id.elemento, nomeList)
                listView.adapter = arrayAdapter
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
        button.setOnClickListener { view ->
            val intent = Intent(this@StasiActivity, MainActivity::class.java)
            intent.putExtra("usSt",us)      //portiamo avanti il nome dello Sportivo in modo da autocopilare il suo nome nella prenotazione che farà
            startActivity(intent)

        }
    }
}