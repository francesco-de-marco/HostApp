package com.example.hostapp

import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hostapp.databinding.ActivityMainBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.Calendar

class DailyCalendarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var listAdapter: ListAdapter
    private lateinit var listData: ListData
    var dataArrayList = ArrayList<ListData?>()
    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private lateinit var calendario: Calendar
    private lateinit var dataSel: TextView
    private lateinit var inputTimeI: EditText
    private lateinit var inputTimeF: EditText
    private lateinit var timeSelect: TextView
    private lateinit var btnConferma: Button
    private lateinit var nomeSportivo: EditText
    private lateinit var picker: TimePickerDialog
    private var hourI:Int = 0
    private var hourF:Int = 0

    fun verificaPrenotazione(attivita: String,dataPren: String,nomeSportivo: String){

        val orario="${inputTimeI.text}-${inputTimeF.text}"
        val chiaveConfronto = orario+" "+dataPren

        database = FirebaseDatabase.getInstance()
        reference = database.getReference("prenotazioni/$attivita")

        reference.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val dataSnapshot = task.result
                val prenotati = dataSnapshot.children.map { it.getValue(OrarioClass::class.java) }

                val giornoDaControllare = dataPren // Sostituisci con il giorno da controllare
                val oraDaControllare = orario // Sostituisci con l'ora da controllare

                // Controlla ogni orario
                for (pren in prenotati) {
                    if (pren?.giorno == giornoDaControllare && pren?.ora == oraDaControllare) {
                        inputTimeI.error = "Orario occupato"
                        inputTimeI.requestFocus()
                        return@addOnCompleteListener
                    } else
                        reference.child(chiaveConfronto).setValue(OrarioClass(nomeSportivo, dataPren, orario))
                }
            }else{

            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily_calendar)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var dataPren = intent.getStringExtra(Intent.EXTRA_TEXT).toString()

        //var attivita = intent.getStringExtra(Intent.EXTRA_TEXT).toString()
        var attivita = "Filippo"

        dataSel = findViewById(R.id.dataSel)
        dataSel.setText(dataPren)
        calendario = Calendar.getInstance()
        btnConferma=findViewById(R.id.conferma)
        //Ora
        inputTimeI = findViewById(R.id.inputTimeI)
        inputTimeF = findViewById(R.id.inputTimeF)
        timeSelect = findViewById(R.id.timeSelect)
        nomeSportivo = findViewById(R.id.nomeSportivo)

        inputTimeI.setInputType(InputType.TYPE_NULL);
        inputTimeI.setOnClickListener{
            hourI = calendario[Calendar.HOUR_OF_DAY]
            picker = TimePickerDialog(it.context, object : TimePickerDialog.OnTimeSetListener {
                override fun onTimeSet(view: TimePicker, sHour: Int, sMinute: Int) {
                    if (sMinute!=0)
                        Toast.makeText(this@DailyCalendarActivity, "I minuti verrano ignorati", Toast.LENGTH_SHORT).show()
                    hourI=sHour
                    inputTimeI.setText(""+sHour + ":" + "00")
                } }, hourI, 0, true)
            picker.show()
        }
        inputTimeF.setInputType(InputType.TYPE_NULL);
        inputTimeF.setOnClickListener{
            hourF = calendario[Calendar.HOUR_OF_DAY]

            picker = TimePickerDialog(it.context, object : TimePickerDialog.OnTimeSetListener {
                override fun onTimeSet(view: TimePicker, sHour: Int, sMinute: Int) {
                    if(sHour==0)
                        hourF=24
                    else hourF=sHour
                    if (sMinute!=0)
                        Toast.makeText(this@DailyCalendarActivity, "I minuti verrano ignorati", Toast.LENGTH_SHORT).show()
                    inputTimeF.setText(""+hourF + ":" + "00")
                } }, hourF, 0, true)
            picker.show()
        }
/*
        var prenotaList = arrayOf("Ciccio","Franchino","Giancarlo")

        database = FirebaseDatabase.getInstance()
        reference = database.getReference("prenotazioni/$attivita")

        reference.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val dataSnapshot = task.result
                val prenotati = dataSnapshot.children.map { it.getValue(OrarioClass::class.java) }

                for (pren in prenotati) {
                    if(pren?.giorno == dataPren)
                        prenotaList += "${pren?.ora ?: ""} ${pren?.giorno ?: ""} ${pren?.nome ?: ""}"
                }
            }else{

            }
        }

        for (i in prenotaList.indices) {
            listData = ListData(
                prenotaList[i],
            )
            dataArrayList.add(listData)
        }

        listAdapter = ListAdapter(this@DailyCalendarActivity, dataArrayList)
        binding.listview.adapter = listAdapter

        btnConferma.setOnClickListener { view ->
            if(hourI>=hourF){
                Toast.makeText(this@DailyCalendarActivity, "L'intervvallo di tempo è scoretto", Toast.LENGTH_SHORT).show()
                inputTimeI.requestFocus()
            } else if (hourF != (hourI+1)){
                Toast.makeText(this@DailyCalendarActivity, "L'intervvallo di tempo deve essere di 1 ora", Toast.LENGTH_SHORT).show()
                inputTimeI.requestFocus()
            }else if (nomeSportivo.text.toString() == "" ){
                nomeSportivo.error="Inserire nome"
                nomeSportivo.requestFocus()
            }else {
                timeSelect.text = "L'intervallo è: [${inputTimeI.text}-${inputTimeF.text}]"
                verificaPrenotazione(attivita,dataPren,nomeSportivo.text.toString())
            }
        }

*/

    }


}

