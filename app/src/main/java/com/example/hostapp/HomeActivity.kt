package com.example.hostapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast

class HomeActivity : AppCompatActivity() {

    private lateinit var radioTipo: RadioGroup
    private lateinit var radioSport: RadioButton
    private lateinit var radioGest: RadioButton
    private lateinit var btnAvanti: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        radioTipo = findViewById(R.id.radioTipo)
        radioSport = findViewById(R.id.radioSport)
        radioGest = findViewById(R.id.radioGest)
        btnAvanti = findViewById(R.id.btnAvanti)

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

                val intent = Intent(this, RegisterActivity::class.java)
                intent.putExtra(Intent.EXTRA_TEXT, tipo)
                startActivity(intent)

            }
        }

    }
}