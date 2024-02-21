package com.example.hostapp

import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Calendar

class DailyCalendarActivity : AppCompatActivity() {

    private lateinit var listaPren: ListView
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
    private lateinit var nome: String
    fun listaPren(attivita: String,dataPren: String){
        listaPren = findViewById(R.id.listPre)
        var nomeList = mutableListOf<String>()


        reference = database.getReference("prenotazioni/$attivita/$dataPren")
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (childSnapshot in snapshot.children) {
                    val attributo1 = childSnapshot.child("nome").getValue(String::class.java)
                    val attributo2 = childSnapshot.child("ora").getValue(String::class.java)
                    val resultString = "$attributo1 - ore: $attributo2"
                    nomeList.add(resultString)
                }

                if(nomeList.size==0)
                    nomeList.add("Nessuna Prenotazione al momento")

                val arrayAdapter = ArrayAdapter(this@DailyCalendarActivity, R.layout.lista, R.id.elemento, nomeList)
                listaPren.adapter = arrayAdapter
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
    fun verificaPrenotazione(attivita: String,dataPren: String,nomeSportivo: String){

        val orario="${inputTimeI.text}-${inputTimeF.text}"

        database = FirebaseDatabase.getInstance()
        reference = database.getReference("prenotazioni/$attivita/")

        reference.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                reference = database.getReference("prenotazioni/$attivita/$dataPren")
                reference.get().addOnCompleteListener { task ->
                    if( task.isSuccessful){
                        val checkOra = reference.orderByChild("ora").equalTo(orario)
                        checkOra.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (snapshot.exists()) {
                                    inputTimeI.error = "Orario occupato"
                                    inputTimeI.requestFocus()
                                }else {
                                    reference.child(orario).setValue(OrarioClass(nomeSportivo, dataPren, orario))
                                    Toast.makeText(this@DailyCalendarActivity, "Prenotazione effettuata con successo", Toast.LENGTH_SHORT).show()
                                }
                            }
                            override fun onCancelled(databaseError: DatabaseError) {
                                // Handle possible errors.
                            }
                        })
                    } else{
                        reference.child(dataPren).child(orario).setValue(OrarioClass(nomeSportivo, dataPren, orario))
                        Toast.makeText(this@DailyCalendarActivity, "Prenotazione effettuata con successo", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        if(nome!=null){
            val intent = Intent(this@DailyCalendarActivity, Main2Activity::class.java)
            intent.putExtra("user3",nome)
            intent.putExtra("attivita",attivita)
            startActivity(intent)
        }else{
            val intent = Intent(this@DailyCalendarActivity, MainActivity::class.java)
            intent.putExtra("user",nome)
            startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily_calendar)

        database = FirebaseDatabase.getInstance()
        reference = database.getReference("prenotazioni")

        nome = intent.getStringExtra("user4").toString()
        var dataPren = intent.getStringExtra("dataPren")


        dataSel = findViewById(R.id.dataSel)
        dataSel.setText(dataPren)
        calendario = Calendar.getInstance()
        btnConferma=findViewById(R.id.conferma)
        //Ora
        inputTimeI = findViewById(R.id.inputTimeI)
        inputTimeF = findViewById(R.id.inputTimeF)
        timeSelect = findViewById(R.id.timeSelect)
        nomeSportivo = findViewById(R.id.nomeSportivo)

        if(nome!=null){
            nomeSportivo.setText(nome)
        }
        var attivita = intent.getStringExtra("attivita2")


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
        listaPren(attivita!!,dataPren!!)

        btnConferma.setOnClickListener { view ->
            if(hourI>=hourF){
                Toast.makeText(this@DailyCalendarActivity, "L'intervallo di tempo è scoretto", Toast.LENGTH_SHORT).show()
                inputTimeI.requestFocus()
            } else if (hourF != (hourI+1)){
                Toast.makeText(this@DailyCalendarActivity, "L'intervallo di tempo deve essere di 1 ora", Toast.LENGTH_SHORT).show()
                inputTimeI.requestFocus()
            }else if (nomeSportivo.text.toString() == "" ){
                nomeSportivo.error="Inserire nome"
                nomeSportivo.requestFocus()
            }else {
                timeSelect.text = "L'intervallo è: [${inputTimeI.text}-${inputTimeF.text}]"
                verificaPrenotazione(attivita!!,dataPren!!,nomeSportivo.text.toString())
            }
        }
    }
}