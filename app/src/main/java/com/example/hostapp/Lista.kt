package com.example.hostapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class Lista : AppCompatActivity() {         //per implementare la listView semplice
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lista)
    }
}