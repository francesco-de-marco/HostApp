package com.example.hostapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {

    private lateinit var radioTipo: RadioGroup
    private lateinit var radioSport: RadioButton
    private lateinit var radioGest: RadioButton
    private lateinit var btnAvanti: Button
    private lateinit var haGiaAccount: TextView
    override fun onCreate(savedInstanceState: Bundle?) {    //differenzia i tipi di registrazione
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        radioTipo = findViewById(R.id.radioTipo)
        radioSport = findViewById(R.id.radioSport)
        radioGest = findViewById(R.id.radioGest)
        btnAvanti = findViewById(R.id.btnAvanti)
        haGiaAccount = findViewById(R.id.haGiaAccount)

        val accountEsistente = findViewById<TextView>(R.id.haGiaAccount)
        accountEsistente.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        var tipo = ""
        radioTipo.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioSport -> {
                    tipo = "Sportivo"
                    Toast.makeText(
                        this@HomeActivity,
                        "Visualizza le strutture nelle tue vicinanze",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                R.id.radioGest -> {
                    tipo = "Gestore"
                    Toast.makeText(
                        this@HomeActivity,
                        "Registra la tua Attività! Fatti Raggiungere!!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        btnAvanti.setOnClickListener { view ->
            if (tipo == "") {
                Toast.makeText(
                    this@HomeActivity,
                    "Devi selezionare una tipologia di registrazione!",
                    Toast.LENGTH_LONG
                ).show()
                radioTipo.requestFocus()
            }else{
                if(tipo=="Sportivo"){
                    val intent = Intent(this, RegisterActivity::class.java)
                    startActivity(intent)
                }else{
                    val intent = Intent(this, GestoreActivity::class.java)
                    startActivity(intent)
                }



            }
        }

    }
}