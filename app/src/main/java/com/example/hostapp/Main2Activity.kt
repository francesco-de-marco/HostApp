package com.example.hostapp


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar


class Main2Activity : AppCompatActivity() {     //mostra un calendario per selezionare la data in cui si vuole prenotare

    private lateinit var calendarView: CalendarView
    private lateinit var calendario: Calendar
    private lateinit var btnPrenotati: Button
    private lateinit var dataPren: TextView
    private lateinit var activity: String

    data class SelectedDate(val year: Int, val month: Int, val day: Int)
    var selectedDate: SelectedDate? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        //Caledario
        calendarView = findViewById(R.id.calendarView)
        calendario = Calendar.getInstance()
        btnPrenotati=findViewById(R.id.prenotati)
        dataPren=findViewById(R.id.dataPren)

        //selezione calendario
        calendarView.setOnDateChangeListener { view, year, month, day->
            selectedDate = SelectedDate(year, month, day)
            dataPren.setText(""+day+"-"+(month+1)+"-"+year)

        }
        var username=intent.getStringExtra("user3")
        
        if(username!=null){     //questo controllo serve per differenziare quando una prenotazione viene fatta da uno Sportivo o dal Gestore per il suo campo
            activity= intent.getStringExtra("luogo").toString()
        }else{
            activity= intent.getStringExtra("attivita").toString()      // se è null vorrà dire che la prima activity avviata dopo il Login è questa
        }

        btnPrenotati.setOnClickListener { view ->
            if (selectedDate == null) {
                dataPren.requestFocus()
                dataPren.error
                Toast.makeText(this@Main2Activity, "Devi selezionare una data", Toast.LENGTH_SHORT).show()
            } else if (dataScorretta()) {
                dataPren.requestFocus()
                dataPren.error
                Toast.makeText(this@Main2Activity, "La data selezionata è sbagliata", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, DailyCalendarActivity::class.java)
                if(username==null){
                    intent.putExtra("dataPren", dataPren.text.toString())
                    intent.putExtra("attivita2", activity)
                }else{
                    intent.putExtra("dataPren", dataPren.text.toString())
                    intent.putExtra("attivita2", activity)
                    intent.putExtra("user4",username)
                }
                startActivity(intent)
            }
        }
    }

    private fun dataScorretta(): Boolean {
        val yearC = calendario.get(Calendar.YEAR) //anno di oggi
        val monthC = calendario.get(Calendar.MONTH)//mese di oggi
        val dayC = calendario.get(Calendar.DAY_OF_MONTH)//giorno di oggi
        return  (selectedDate!!.year<yearC || selectedDate!!.year>yearC+1 ) ||
                (selectedDate!!.year==yearC && selectedDate!!.month<monthC) ||
                (selectedDate!!.year==yearC && selectedDate!!.month==monthC && selectedDate!!.day<dayC)

    }




}