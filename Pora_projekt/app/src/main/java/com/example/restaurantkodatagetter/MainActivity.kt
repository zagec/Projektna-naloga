package com.example.restaurantkodatagetter

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantkodatagetter.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), dataAdapter.onNodeListener, SensorEventListener {
    lateinit var binding: ActivityMainBinding
    var dataArr: MutableList<Data> = mutableListOf()

    //steps
    private var sensorManager: SensorManager? = null
    private var running = false
    private var totalSteps = 0f
    private var previousTotalSteps = 0f


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //dodaj svojo data thingy (time je v min pomojem najboljse)
        dataArr.add(
            Data(
                "Number of People in restaurant",
                "Gets number of people from an image",
                30,
                "Location of the restaurant",
                true
            )
        )
        dataArr.add(
            Data(
                "Number of Cars on the roads to the restaurant",
                "Get number of cars from an image",
                25,
                "Owners location",
                true
            )
        )

        //steps
        dataArr.add(Data("Number of steps", "Counts your steps", 2, "Your location", true))


        val dataArrClickListener = { position: Int ->
            println(dataArr[position].enabled)
        }
        val rvConcerts = binding.dataRV //as RecyclerView
        val adapterConcertView = dataAdapter(dataArr, this, dataArrClickListener)
        rvConcerts.adapter = adapterConcertView
        rvConcerts.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        //steps
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    //steps
    override fun onResume() {
        super.onResume()
        running = true
        // This sensor requires permission android.permission.ACTIVITY_RECOGNITION. in AndroidManifest.xml
        val stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        if (stepSensor != null) {
            sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
        } else {
            Toast.makeText(this, "No sensor detected on this device", Toast.LENGTH_SHORT).show()
        }
    }

    //steps
    override fun onSensorChanged(event: SensorEvent?) {

        // klicemo textview v recyclerju ce bi hotel displajat stevilo korakov
        //var tv_stepsTaken = findViewById<TextView>(R.id.tv_stepsTaken)

        if (running) {
            totalSteps = event!!.values[0]
            val currentSteps =
                totalSteps.toInt() - previousTotalSteps.toInt() //vzamemo stevilo vseh stepou in jim odstejemo trenutne
            // za prikaz stepov?
            //tv_stepsTaken.text = ("$currentSteps")
        }
    }

    // steps
    //reset steps on a set timer
    fun resetSteps() {
        //only need this if steps are displayed
        //var tv_stepsTaken = findViewById<TextView>(R.id.tv_stepsTaken)
        previousTotalSteps = totalSteps
        // the steps will be reset to 0
        //tv_stepsTaken.text = 0.toString()
        // Send data to db
        //saveData()
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }

    override fun onNoteClick(position2: Int) {
        println("click")
    }

    override fun onNodeLongClick(position2: Int) {
        println("click")
    }
}