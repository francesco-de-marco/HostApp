package com.example.hostapp


import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import java.util.Calendar
import android.text.InputType;
import android.widget.TimePicker;


class Main2Activity : AppCompatActivity() {

    private lateinit var calendarView: CalendarView
    private lateinit var calendario: Calendar
    private lateinit var btnPrenotati: Button
    private lateinit var dataPren: TextView
    private lateinit var btnGetTime: Button
    private lateinit var inputTimeI: EditText
    private lateinit var inputTimeF: EditText
    private lateinit var timeSelect: TextView
    private lateinit var picker:TimePickerDialog
    private var hourI:Int = 0
    private var hourF:Int = 0


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
        //Ora
        inputTimeI = findViewById(R.id.inputTimeI)
        inputTimeF = findViewById(R.id.inputTimeF)
        btnGetTime=findViewById(R.id.btnGetTime)
        timeSelect=findViewById(R.id.timeSelect)
        //selezione calendario
        calendarView.setOnDateChangeListener { view, year, month, day->
            selectedDate = SelectedDate(year, month, day)
            dataPren.setText("Data: "+day+"/"+(month+1)+"/"+year)

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
            } else if(hourI>=hourF)
                    Toast.makeText(this@Main2Activity, "L'intervvallo di tempo è scoretto", Toast.LENGTH_SHORT).show()
        }

        inputTimeI.setInputType(InputType.TYPE_NULL);
        inputTimeI.setOnClickListener{
            hourI = calendario[Calendar.HOUR_OF_DAY]

            picker = TimePickerDialog(it.context, object : TimePickerDialog.OnTimeSetListener {
                override fun onTimeSet(view: TimePicker, sHour: Int, sMinute: Int) {
                    if (sMinute!=0)
                        Toast.makeText(this@Main2Activity, "I minuti verrano ignorati", Toast.LENGTH_SHORT).show()
                    hourI=sHour
                    inputTimeI.setText(""+sHour + ":" + "00")
                }
            }, hourI, 0, true)
            picker.show()
        }
        inputTimeF.setInputType(InputType.TYPE_NULL);
        inputTimeF.setOnClickListener{
            hourF = calendario[Calendar.HOUR_OF_DAY]

            picker = TimePickerDialog(it.context, object : TimePickerDialog.OnTimeSetListener {
                override fun onTimeSet(view: TimePicker, sHour: Int, sMinute: Int) {
                    if (sMinute!=0)
                        Toast.makeText(this@Main2Activity, "I minuti verrano ignorati", Toast.LENGTH_SHORT).show()
                    if(sHour==0)
                        hourF=24
                    else hourF=sHour
                    inputTimeF.setText(""+hourF + ":" + "00")
                }
            }, hourF, 0, true)
            picker.show()
        }
        val btnGetTime: Button = findViewById(R.id.btnGetTime)
        btnGetTime.setOnClickListener {
            if(hourI>=hourF)
                Toast.makeText(this@Main2Activity, "["+hourI+"-"+hourF+"] L'intervvallo di tempo è scoretto", Toast.LENGTH_SHORT).show()
            else
                timeSelect.text = "L'intervallo è: [${inputTimeI.text}-${inputTimeF.text}]"
        }
    }


    private fun dataScorretta(): Boolean {
        val yearC = calendario.get(Calendar.YEAR) //anno di oggi
        val monthC = calendario.get(Calendar.MONTH)//
        val dayC = calendario.get(Calendar.DAY_OF_MONTH)
        return  (selectedDate!!.year<yearC || selectedDate!!.year>yearC+1 ) ||
                (selectedDate!!.year==yearC && selectedDate!!.month<monthC) ||
                (selectedDate!!.year==yearC && selectedDate!!.month==monthC && selectedDate!!.day<dayC)

    }




}