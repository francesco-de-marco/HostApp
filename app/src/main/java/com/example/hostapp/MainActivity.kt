package com.example.hostapp

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.app.AppCompatActivity
import com.example.hostapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {      //Permette allo Sportivo di scegliere lo sport del campo che vuole prenotare

    private lateinit var binding: ActivityMainBinding
    private lateinit var listAdapter: ListAdapter
    private lateinit var listData: ListData
    var dataArrayList = ArrayList<ListData?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val nameList = arrayOf("Calcio a 5", "Calcio a 7", "Basket", "Pallavolo", "Padel", "Golf", "Tennis", "Tiro a segno")


        for (i in nameList.indices) {
            listData = ListData(
                nameList[i]
            )
            dataArrayList.add(listData)
        }
        var us= intent.getStringExtra("usSt")
        listAdapter = ListAdapter(this@MainActivity, dataArrayList)
        binding.listview.adapter = listAdapter
        binding.listview.isClickable = true
        binding.listview.onItemClickListener = OnItemClickListener { adapterView, view, i, l ->
            val intent = Intent(this@MainActivity, MapsActivity::class.java)
            intent.putExtra("user2",us)
            intent.putExtra("name", nameList[i])
            startActivity(intent)

        }

    }

}