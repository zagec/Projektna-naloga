package com.example.restaurantkodatagetter

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import java.util.*

const val MY_SP_FILE_NAME = "myshared.data"

class MyApplication: Application() {
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(){
        super.onCreate()
        initShared()
        if (!containsID()) {
            saveID(UUID.randomUUID().toString().replace("-", ""))
        }
    }

    fun initShared() {
        sharedPref = getSharedPreferences( MY_SP_FILE_NAME, Context.MODE_PRIVATE)
    }

    fun saveID(id:String) {
        with (sharedPref.edit()) {
            putString("ID", id)
            apply()
        }
    }
    fun containsID():Boolean {
        return sharedPref.contains("ID")
    }
    fun getID(): String? {
        return sharedPref.getString("ID","DefaultNoData")
    }
}