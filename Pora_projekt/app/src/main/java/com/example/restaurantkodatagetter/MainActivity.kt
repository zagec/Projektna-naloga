package com.example.restaurantkodatagetter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantkodatagetter.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), dataAdapter.onNodeListener {
    lateinit var binding: ActivityMainBinding
    var dataArr: MutableList<Data> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //dodaj svojo data thingy (time je v min pomojem najboljse)
        dataArr.add(Data("Number of People in restaurant", "Gets number of people from an image", 30, "Location of the restaurant", true))

        val dataArrClickListener = { position: Int ->
            println(dataArr[position].enabled)
        }
        val rvConcerts = binding.dataRV as RecyclerView
        val adapterConcertView = dataAdapter(dataArr,this, dataArrClickListener)
        rvConcerts.adapter = adapterConcertView
        rvConcerts.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    override fun onNoteClick(position2: Int) {
        println("click")
    }

    override fun onNodeLongClick(position2: Int) {
        println("click")
    }
}