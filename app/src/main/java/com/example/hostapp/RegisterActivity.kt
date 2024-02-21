package com.example.hostapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        inputEmail = findViewById(R.id.inputEmail)
        inputUser = findViewById(R.id.inputUser)
        inputPassword = findViewById(R.id.inputPassword)
        inputConfirmPass = findViewById(R.id.inputConfirmPass)
        haGiaAccount = findViewById(R.id.haGiaAccount)
        btnRegister = findViewById(R.id.btnRegister)

        val tipo = "Sportivo"

        val accountEsistente = findViewById<TextView>(R.id.haGiaAccount)
        accountEsistente.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        fun registrazione(user: String,email: String, password: String, tipo: String){
            val helperClass = HelperClass(user, email, password, tipo)
            reference.child(user).setValue(helperClass)
            Toast.makeText(this@RegisterActivity, "Ti sei registrato con successo", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

        }

        fun approvato(){
            val regUser = Regex("^[a-zA-Z0-9_]{3,}")
            val regEmail = Regex("^[a-zA-Z0-9.!#\$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*")
            val regPass = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\$%^&*])[a-zA-Z0-9!@#\$%^&*]{7,}\$")

            database = FirebaseDatabase.getInstance()
            reference = database.getReference("users")
            val user = inputUser.text.toString()
            val email = inputEmail.text.toString()
            val password = inputPassword.text.toString()
            var conferma= inputConfirmPass.text.toString()
            if (user=="") {
                inputUser.error = "Devi compilare tutti i campi"
                inputUser.requestFocus()
            }else if (email=="") {
                inputEmail.error = "Devi compilare tutti i campi"
                inputEmail.requestFocus()
            }
            else if (password=="") {
                inputPassword.error = "Devi compilare tutti i campi"
                inputPassword.requestFocus()
            }else if(!regUser.matches(user)){
                inputUser.error = "User di almeno 3 lettere"
                inputUser.requestFocus()
            }else if(!regEmail.matches(email)){
                inputEmail.error = "Email non valida"
                inputEmail.requestFocus()
            }else if(regPass.matches(password)){
                inputPassword.error = "Password poco efficiente"
                inputPassword.requestFocus()
            }else if (!conferma.equals(password)){
                 inputConfirmPass.setText("")
                 inputConfirmPass.error = "Password Non Coincide"
                 inputConfirmPass.requestFocus()
             }
            else registrazione(user,email,password, tipo)
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