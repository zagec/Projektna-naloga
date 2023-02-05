package com.example.restaurantkodatagetter

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Base64.encodeToString
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.restaurantkodatagetter.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.FirebaseDatabase

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.create
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import org.json.JSONObject
import java.io.*


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
    private var mDatabase: FirebaseDatabase = FirebaseDatabase.getInstance("https://beasty-e3c46-default-rtdb.europe-west1.firebasedatabase.app/")
    private lateinit var locationManager: LocationManager

    private val client = OkHttpClient()
    val JSON: MediaType = "application/json; charset=utf-8".toMediaType()


    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                handleCameraImage(result.data)
            }
        }
    }

    companion object {
        private val MEDIA_TYPE_PNG = "image/png".toMediaType()
    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "naslov", null)
        return Uri.parse(path)
    }

    //https://tools.knowledgewalls.com/base64-to-image
    @RequiresApi(Build.VERSION_CODES.O)
    private fun handleCameraImage(intent: Intent?) {
        val bitmap = intent?.extras?.get("data") as Bitmap

        val wrapper = ContextWrapper(applicationContext)

        // Initializing a new file
        // The bellow line return a directory in internal storage
        var file : File = wrapper.getDir("images", Context.MODE_PRIVATE)


        // Create a file to save the image
        file = File(file, "${UUID.randomUUID()}.png")



        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()


        val jsonData = JSONObject()
        jsonData.put("image", Base64.getEncoder().encodeToString(byteArray))

        val formBody = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(),
            jsonData.toString()
        )


        println(getImageUri(this,bitmap).toString())
        val request = Request.Builder()
            .url("https://beasty-e3c46-default-rtdb.europe-west1.firebasedatabase.app/image.json")
            .post(formBody)
            .build()

        try {
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    println("napaka")
                }
                override fun onResponse(call: Call, response: Response) {
                    val body = response.body?.string()
                    println(body)

                    Snackbar.make(binding.btnTakePicture, "Uspešno poslano",Snackbar.LENGTH_LONG).show()
                }
            })
        } catch(err: Error) {
            print("Error when executing get request: " + err.localizedMessage)
        }

        val myFile = File("./map.png")
        myFile.delete()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = application as MyApplication
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

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

        dataArr.add(
            Data(
                "Number of steps",
                "Counts your steps",
                2,
                "Your location",
                true
            )
        )

        binding.btnTakePicture.setOnClickListener {
            if(isCameraPermissionGranted()) {
                val intent: Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                resultLauncher.launch(intent)
                //startActivityForResult(intent, 200)
            }
        }

        binding.btnCars.setOnClickListener {
            sendCars()
        }

        binding.btnSteps.setOnClickListener {
//            Log.i("asd", "i clicked the button")
//            var dbref = mDatabase.getReference("steps")
//            val key: String? = dbref.push().getKey()
//            Log.i("asd", key.toString())
//
//            if(key != null) {
//                var err = dbref.child(key).setValue("First item")
//                if(err.isSuccessful){
//
//                }else {
//                    Log.i("exception", err.exception.toString())
//                    Log.i("err", err.toString())
//                    Log.i("complete", err.isComplete.toString())
//                }
//                Log.i("asd", "asd")
//            } else {
//                Log.i("asd", "i clicked the button")
//            }
            sendNumberOfSteps()
        }
        binding.btnPeople.setOnClickListener {
            sendPeople()
        }

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
        if(dataArr[1].enabled) {
            timer.schedule(timerForCar(), 0, (dataArr[1].time * 60000).toLong())
        }
        if(dataArr[2].enabled) {
            timer.schedule(timerForSteps(), 0, (dataArr[2].time * 60000).toLong())
        }
        if(dataArr[0].enabled) {
            timer.schedule(timerForPeople(), 0, (dataArr[0].time * 60000).toLong())
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun sendNumberOfSteps() {
        var dbref = mDatabase.getReference("steps")
        if (dataArr[2].enabled) {
            val number = (0..10).random()
            val steps = Steps(
                UUID.randomUUID().toString(),
                app.getID().toString(),
                number,
                getDateNow(),
                getLocation().toString(),
                dataArr[2].enabled
            )
            dbref.child("steps").child(steps.UUID).setValue(steps)
                .addOnSuccessListener {
                // Write was successful!
                Log.i("firebase","Write was successflul")
                // ...
            }
                .addOnFailureListener {
                    // Write failed
                    // ...
                    Log.i("firebase","Write Failed")
                }
        } else {
            val steps = Steps(
                UUID.randomUUID().toString(),
                app.getID().toString(),
                currentSteps.toInt(),
                getDateNow(),
                getLocation().toString(),
                dataArr[2].enabled
            )
            dbref.child("steps").child(steps.UUID).setValue(steps).addOnSuccessListener {
                // Write was successful!
                Log.i("firebase","Write was successflul")
                // ...
            }
                .addOnFailureListener {
                    // Write failed
                    // ...
                    Log.i("firebase","Write Failed")
                }
            resetSteps()
        }
    }

    private fun timerForSteps(): TimerTask {
        val handler = Handler()

        return object : TimerTask() {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun run() {
                if (dataArr[0].enabled) {
                    handler.post(Runnable {
                        try {
                            sendNumberOfSteps()
                        } catch (_: Exception) {
                        }
                    })
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun sendCars(){
        var dbref = mDatabase.getReference("cars")
        if(dataArr[1].enabled){
            val number = (0..10).random()
            val steps = Cars(
                UUID.randomUUID().toString(),
                app.getID().toString(),
                number.toString(),
                getDateNow(),
                getLocation().toString(),
                dataArr[2].enabled
            )
            dbref.child("cars").child(steps.UUID).setValue(steps)
                .addOnSuccessListener {
                    // Write was successful!
                    Log.i("firebase","Write was successflul")
                    // ...
                }
                .addOnFailureListener {
                    // Write failed
                    // ...
                    Log.i("firebase","Write Failed")
                }
        } else {
            val steps = Cars(
                UUID.randomUUID().toString(),
                app.getID().toString(),
                binding.imageView.toString(),
                getDateNow(),
                getLocation().toString(),
                dataArr[2].enabled
            )
            dbref.child("cars").child(steps.UUID).setValue(steps)
                .addOnSuccessListener {
                    // Write was successful!
                    Log.i("firebase","Write was successflul")
                    // ...
                }
                .addOnFailureListener {
                    // Write failed
                    // ...
                    Log.i("firebase","Write Failed")
                }
        }
    }

    fun timerForCar(): TimerTask {
        val handler = Handler()

        return object : TimerTask() {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun run() {
                if (dataArr[0].enabled) {
                    handler.post(Runnable {
                        try {
                            sendCars()
                        } catch (e: Exception) {
                        }
                    })
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun sendPeople(){
        var dbref = mDatabase.getReference("people")
        if(dataArr[1].enabled){
            val number = (0..10).random()
            val steps = People(
                UUID.randomUUID().toString(),
                app.getID().toString(),
                number.toString(),
                getDateNow(),
                getLocation().toString(),
                dataArr[2].enabled
            )
            dbref.child("people").child(steps.UUID).setValue(steps)
        } else {
            val steps = People(
                UUID.randomUUID().toString(),
                app.getID().toString(),
                binding.imageView.toString(),
                getDateNow(),
                getLocation().toString(),
                dataArr[2].enabled
            )
            dbref.child("people").child(steps.UUID).setValue(steps)
        }
    }

    fun timerForPeople(): TimerTask {
        val handler = Handler()

        return object : TimerTask() {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun run() {
                if (dataArr[1].enabled) {
                    handler.post(Runnable {
                        try {
                            sendPeople()
                        } catch (e: Exception) {
                        }
                    })
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getDateNow(): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        return LocalDateTime.now().format(formatter)
    }

    fun getLocation(): Location? {

        val hasGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val hasNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        if(isLocationPermissionGranted()){
            try{if (hasGps) {
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    5000,
                    0F,
                    gpsLocationListener
                )
                return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            }
//------------------------------------------------------//
                if (hasNetwork) {
                    locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        5000,
                        0F,
                        networkLocationListener
                    )
                    Log.i("has network", "this is true")
                    return locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

                }}catch (_:SecurityException){}
        }
        return null
    }
    val gpsLocationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            //locationByGps= location
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }
    //------------------------------------------------------//
    val networkLocationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            //locationByNetwork= location
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    private fun isCameraPermissionGranted(): Boolean {
        if (!checkCameraHardware(this)){
            return false
        }
        return if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA),100)
            false
        } else {
            true
        }
    }

    private fun checkCameraHardware(context: Context): Boolean {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)
    }

    private fun isLocationPermissionGranted(): Boolean {
        return if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                69
            )
            false
        } else {
            true
        }
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
            currentSteps =
                totalSteps - previousTotalSteps
        }
    }

    // steps
    fun resetSteps() {
        previousTotalSteps = totalSteps
        currentSteps = 0f;
    }

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