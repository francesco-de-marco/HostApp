package com.example.hostapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.FirebaseDatabase

class LoginActivity : AppCompatActivity() {

    private lateinit var loginUser: EditText
    private lateinit var loginPassword: EditText
    private lateinit var registerText: TextView
    private lateinit var btnLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginUser = findViewById(R.id.loginUser)
        loginPassword = findViewById(R.id.loginPassword)
        registerText = findViewById(R.id.registerText)
        btnLogin = findViewById(R.id.btnLogin)

        val textRegister = findViewById<TextView>(R.id.registerText)
        textRegister.setOnClickListener{
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        fun validaUser(): Boolean {
            val cosa = loginUser.text.toString()
            if (cosa.isEmpty()) {
                loginUser.error = "User non può essere vuoto"
                return false
            } else {
                loginUser.error = null
                return true
            }
        }

        fun validaPassword(): Boolean {
            val cosa = loginPassword.text.toString()
            if (cosa.isEmpty()) {
                loginPassword.error = "Password non può essere vuoto"
                return false
            } else {
                loginPassword.error = null
                return true
            }
        }

        fun controllaUser() {
            val user = loginUser.text.toString().trim()
            val userPassword = loginPassword.text.toString().trim()

            val reference = FirebaseDatabase.getInstance().getReference("users")

            val checkUserDatabase = reference.orderByChild("user").equalTo(user)

            checkUserDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        loginUser.error = null
                        val passwordFromDB = snapshot.child(user).child("password").getValue(String::class.java)
                        if (passwordFromDB == userPassword) {
                            loginUser.error = null
                            val intent = Intent(this@LoginActivity, Main2Activity::class.java)
                            startActivity(intent)
                        } else {
                            loginPassword.error = "Credenziali Invalide"
                            loginPassword.requestFocus()
                        }
                    } else {
                        loginUser.error = "User non esiste"
                        loginUser.requestFocus()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle possible errors.
                }
            })
        }

        btnLogin.setOnClickListener { view ->
            if (!validaUser() || !validaPassword()) {
                // Handle invalid user or password
            } else {
                controllaUser()
            }
        }







    }
}