package com.example.restaurantkodatagetter

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.restaurantkodatagetter.databinding.ActivityMainBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*



class MainActivity : AppCompatActivity(), dataAdapter.onNodeListener, SensorEventListener {
    lateinit var binding: ActivityMainBinding
    var dataArr: MutableList<Data> = mutableListOf()
    lateinit var app: MyApplication

    //steps
    private var sensorManager: SensorManager? = null
    private var running = false
    private var totalSteps = 0f
    private var currentSteps = 0f
    private var previousTotalSteps = 0f

    private var stepSensor: Sensor? = null;

    private lateinit var locationManager: LocationManager
    private val locationPermissionCode = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = application as MyApplication
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val db = DatabaseHelper(this, null)

        var dataPeople = getAllFromPeople()
        var dataCars = getAllFromCars()

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
        dataArr.add(Data(
            "Number of steps",
            "Counts your steps",
            2,
            "Your location",
            true))


        binding.btnPeople.setOnClickListener{
            timerFunForPeople(db)
        }

<<<<<<< Updated upstream
=======
        binding.btnCars.setOnClickListener{
            timerFunForCars(db)
        }

        binding.btnSteps.setOnClickListener{
            timerStepCounter(db)
        }

>>>>>>> Stashed changes
        val dataArrClickListener = { position: Int ->
            println(dataArr[position].enabled)
        }
        val rvConcerts = binding.dataRV
        val adapterConcertView = dataAdapter(dataArr, this, dataArrClickListener)
        rvConcerts.adapter = adapterConcertView
        rvConcerts.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        //steps
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        val timer = Timer()
        //600000
        timer.schedule(timerFunForPeople(db), 0, (dataArr[0].time * 60000).toLong()) //execute in every 30 min
    }

    private fun getLocation() {

    }

    fun timerFunForPeople(db: DatabaseHelper): TimerTask{
        val handler = Handler()

        return object : TimerTask() {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun run() {
                if(dataArr[0].enabled){
                    handler.post(Runnable {
                        try {
                            var number = (0..10).random()
                            db.addPeopleNumToDB(number, "test", getDateNow())
                        } catch (e: Exception) {
                        }
                    })
                }
            }
        }
<<<<<<< Updated upstream


=======
    }

    fun timerFunForCars(db: DatabaseHelper): TimerTask{
        val handler = Handler()

        return object : TimerTask() {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun run() {
                if(dataArr[1].enabled){
                    handler.post(Runnable {
                        try {
                            var number = (0..10).random()
                            db.addCarNumToDB(number, "test", getDateNow())
                        } catch (e: Exception) {
                        }
                    })
                }
            }
        }

    }

    fun timerStepCounter(db: DatabaseHelper): TimerTask{
        val mainExecutor = ContextCompat.getMainExecutor(this)
        return object : TimerTask() {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun run() {
                if(dataArr[2].enabled){
                    mainExecutor.execute{
                        val number = (0..10).random()
                        db.addStepCountToDb(number, app.getID().toString(), "test",getDateNow())
                    }
                }
            }
        }
>>>>>>> Stashed changes
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getDateNow() : String{
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        return LocalDateTime.now().format(formatter)
    }


    @SuppressLint("Range")
    fun getAllFromPeople() {
        val db = DatabaseHelper(this, null)
        val cursor = db.getAllFromPeopleNum()

        if (cursor!!.moveToFirst()) {
            var nekaj = mutableListOf(cursor.getString(cursor.getColumnIndex(DatabaseHelper.NUM_COL)), cursor.getString(cursor.getColumnIndex(DatabaseHelper.LOCATION_COL)), cursor.getString(cursor.getColumnIndex(DatabaseHelper.TIME_COL)))
            println(nekaj[0] + " " + nekaj[1] + " " + nekaj[2])
            while(cursor.moveToNext()){
                var nekaj = mutableListOf(cursor.getString(cursor.getColumnIndex(DatabaseHelper.NUM_COL)), cursor.getString(cursor.getColumnIndex(DatabaseHelper.LOCATION_COL)), cursor.getString(cursor.getColumnIndex(DatabaseHelper.TIME_COL)))
                println(nekaj[0] + " " + nekaj[1] + " " + nekaj[2])            }
        }
        cursor.close()
    }

    @SuppressLint("Range")
    fun getAllFromCars() {
        val db = DatabaseHelper(this, null)
        val curs = db.getAllFromCarNum()

        if (curs!!.moveToFirst()) {
            var one = mutableListOf(curs.getString(curs.getColumnIndex(DatabaseHelper.NUM_COL)), curs.getString(curs.getColumnIndex(DatabaseHelper.LOCATION_COL)), curs.getString(curs.getColumnIndex(DatabaseHelper.TIME_COL)))
            println(one[0] + " " + one[1] + " " + one[2])
            while(curs.moveToNext()){
                var two = mutableListOf(curs.getString(curs.getColumnIndex(DatabaseHelper.NUM_COL)), curs.getString(curs.getColumnIndex(DatabaseHelper.LOCATION_COL)), curs.getString(curs.getColumnIndex(DatabaseHelper.TIME_COL)))
                println(two[0] + " " + two[1] + " " + two[2])            }
        }
        curs.close()
    }
    fun stopListening() {
        running = false
        sensorManager?.unregisterListener(this) //always unregister
    }

    fun startListening() {
        running = true
        //sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        if (stepSensor != null) {
            sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
        } else {
            Toast.makeText(this, "No sensor detected on this device", Toast.LENGTH_SHORT).show()
        }
    }

    //steps
    override fun onResume() {
        super.onResume()
        startListening()
    }

    //steps
    override fun onPause() {
        super.onPause()
        stopListening()
    }

    //steps
    override fun onSensorChanged(event: SensorEvent?) {
        if (running) {
            totalSteps = event!!.values[0]
            currentSteps = totalSteps - previousTotalSteps //vzamemo stevilo vseh stepou in jim odstejemo prejsnje total stepse
        }
    }

    // steps
    fun resetSteps() {
        previousTotalSteps = totalSteps
        currentSteps = 0f;
    }
    // after sets are reset save them to db
    // Send data to db
    //saveData()

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }

    override fun onStop() {
        super.onStop()
        stopListening()
    }


    override fun onNoteClick(position2: Int) {
        println("click")
    }

    override fun onNodeLongClick(position2: Int) {
        println("click")
    }
}