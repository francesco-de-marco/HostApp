package com.example.hostapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class GestoreActivity : AppCompatActivity() {

    private lateinit var btnSalva: Button
    private lateinit var inputAttivita: EditText
    private lateinit var inputPhone: EditText
    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gestore)

        btnSalva = findViewById(R.id.btnSalva)
        inputAttivita = findViewById(R.id.inputAttivita)
        inputPhone = findViewById(R.id.inputPhone)

        fun approvato(){
            database = FirebaseDatabase.getInstance()
            reference = database.getReference("location")
            val attivita = inputAttivita.text.toString()
            val phone = inputPhone.text.toString()

            if(attivita=="") {
                inputAttivita.error = "Devi compilare tutti i campi"
                inputAttivita.requestFocus()
            }else if (phone=="") {
                inputPhone.error = "Devi compilare tutti i campi"
                inputPhone.requestFocus()
            } else {
                val helperClass2 = HelperClass2(attivita,phone)
                reference.child(attivita).setValue(helperClass2)
                Toast.makeText(this@GestoreActivity, "Buon Lavoro", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }

        btnSalva.setOnClickListener { view ->
            val attivita = inputAttivita.text.toString().trim()

            val reference = FirebaseDatabase.getInstance().getReference("location")

            val checkUserDatabase = reference.orderByChild("attività").equalTo(attivita)

            checkUserDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        inputAttivita.error="Attività già esistente"
                        inputAttivita.requestFocus()
                    }else approvato()
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle possible errors.
                }
            })
        }
    }
}