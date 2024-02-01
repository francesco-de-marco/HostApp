package com.example.hostapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CalendarView
import android.widget.Toast
import com.google.android.gms.maps.GoogleMap
import java.util.Calendar

class Main2Activity : AppCompatActivity() {

    private lateinit var calendarView: CalendarView
    private lateinit var calendario: Calendar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        calendarView = findViewById(R.id.calendarView)
        calendario = Calendar.getInstance()

        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            Toast.makeText(this@Main2Activity, "$dayOfMonth/${month + 1}/$year", Toast.LENGTH_SHORT).show()
        }



    }
}