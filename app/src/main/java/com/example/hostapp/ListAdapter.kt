package com.example.hostapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
class ListAdapter(context: Context, dataArrayList: ArrayList<ListData?>?) :
    ArrayAdapter<ListData?>(context, R.layout.list_item, dataArrayList!!) {
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        var view = view
        val listData = getItem(position)
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        }
        val listName = view!!.findViewById<TextView>(R.id.listName)


        if (listData != null) {
            listName.text = listData.name
        }

        return view!!
    }
}

class ListData(
    var name: String)