package com.example.hostapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RegisterActivity : AppCompatActivity() {

    private lateinit var inputEmail: EditText
    private lateinit var inputUser: EditText
    private lateinit var inputPassword: EditText
    private lateinit var inputConfirmPass: EditText
    private lateinit var haGiaAccount: TextView
    private lateinit var btnRegister: Button
    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private lateinit var radioTipo: RadioGroup
    private lateinit var radioSport: RadioButton
    private lateinit var radioGest: RadioButton
    private var tipo: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        inputEmail = findViewById(R.id.inputEmail)
        inputUser = findViewById(R.id.loginUser)
        inputPassword = findViewById(R.id.loginPassword)
        inputConfirmPass = findViewById(R.id.inputConfirmPass)
        radioTipo = findViewById(R.id.radioTipo)
        radioSport = findViewById(R.id.radioSport)
        radioGest = findViewById(R.id.radioGest)
        haGiaAccount = findViewById(R.id.haGiaAccount)
        btnRegister = findViewById(R.id.btnRegister)

        radioTipo.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioSport -> {
                    tipo=false
                    Toast.makeText(this@RegisterActivity, "Visualizza le strutture nelle tue vicinanze", Toast.LENGTH_SHORT).show()
                }
                R.id.radioGest -> {
                    tipo=true
                    Toast.makeText(this@RegisterActivity, "Registra la tua Attività! Fatti Raggiungere!!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        val accountEsistente = findViewById<TextView>(R.id.haGiaAccount)
        accountEsistente.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        fun registrazione(user: String,email: String, password: String){
            val helperClass = HelperClass(user, email, password,tipo)
            reference.child(user).setValue(helperClass)
            Toast.makeText(this@RegisterActivity, "Ti sei registrato con successo", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        fun approvato(){
            database = FirebaseDatabase.getInstance()
            reference = database.getReference("users")
            val user = inputUser.text.toString()
            val email = inputEmail.text.toString()
            val password = inputPassword.text.toString()

            var conferma= inputConfirmPass.text.toString()
            if (!conferma.equals(password)) {
                inputConfirmPass.setText("")
                conferma= inputConfirmPass.text.toString()
                inputConfirmPass.error = "Password Non Coincide"
                inputConfirmPass.requestFocus()
            }else registrazione(user,email,password)
        }

        btnRegister.setOnClickListener { view ->
            val user = inputUser.text.toString().trim()

            val reference = FirebaseDatabase.getInstance().getReference("users")

            val checkUserDatabase = reference.orderByChild("user").equalTo(user)

            checkUserDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        inputUser.error="User già esistente"
                        inputUser.requestFocus()
                    }else approvato()
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle possible errors.
                }
            })
        }



    }


}