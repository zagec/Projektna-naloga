package com.example.restaurantkodatagetter

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val queryTime = ("CREATE TABLE " + PEOPLE_NUM_TABLE + " ("
                + ID_COL + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                NUM_COL + " INT NOT NULL," +
                TIME_COL + " TEXT NOT NULL," +
                LOCATION_COL + " TEXT NOT NULL" + ");"
                )

        val bestRoad = ("CREATE TABLE " + CAR_NUM_TABLE + " ("
                + ID_COL + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                NUM_COL + " INT NOT NULL," +
                TIME_COL + " TEXT NOT NULL," +
                LOCATION_COL + " TEXT NOT NULL" + ");"
                )

        val steps = ("CREATE TABLE " + STEPS + " ("
                + ID_COL + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                NUM_COL + " INT NOT NULL," +
                USER_ID + " TEXT NOT NULL," +
                TIME_COL + " TEXT NOT NULL," +
                LOCATION_COL + " TEXT NOT NULL" + ");"
                )

        db.execSQL(queryTime)
        db.execSQL(bestRoad)
        db.execSQL(steps)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("DROP TABLE IF EXISTS ${PEOPLE_NUM_TABLE}")
        db.execSQL("DROP TABLE IF EXISTS ${CAR_NUM_TABLE}")
        db.execSQL("DROP TABLE IF EXISTS $STEPS")
        onCreate(db)
    }

    // ------------------ CAR COUNT --------------------------------------

    fun addCarNumToDB(num: Int, location: String, date: String){
        val values = ContentValues()

        values.put(NUM_COL, num)
        values.put(LOCATION_COL, location)
        values.put(TIME_COL, date)

        val db = this.writableDatabase

        db.insert(CAR_NUM_TABLE, null, values)
        db.close()
    }

    fun clearCartable() {
        val db = this.readableDatabase

        val clearDBQuery = "DELETE FROM $CAR_NUM_TABLE"
        db.execSQL(clearDBQuery)
    }

    fun getAllFromCarNum(): Cursor? {
        val db = this.readableDatabase

        val queryGetAll = "SELECT * FROM ${CAR_NUM_TABLE}"

        return db.rawQuery(queryGetAll, null)
    }

    fun addStepCountToDb(count: Int,userId: String ,location: String, date: String){
        val values = ContentValues()

        values.put(NUM_COL, count)
        values.put(USER_ID, userId)
        values.put(LOCATION_COL, location)
        values.put(TIME_COL, date)

        val db = this.writableDatabase

        db.insert(CAR_NUM_TABLE, null, values)
        db.close()
    }
    fun clearStepsCountTable() {
        val db = this.readableDatabase

        val clearDBQuery = "DELETE FROM $STEPS"
        db.execSQL(clearDBQuery)
    }

    fun getAllFromStepsCount(): Cursor? {
        val db = this.readableDatabase

        val queryGetAll = "SELECT * FROM $STEPS"

        return db.rawQuery(queryGetAll, null)
    }

    // ------------------ PEOPLE COUNT --------------------------------------

    fun addPeopleNumToDB(num: Int, location: String, date: String){
        val values = ContentValues()

        values.put(NUM_COL, num)
        values.put(LOCATION_COL, location)
        values.put(TIME_COL, date)

        val db = this.writableDatabase

        db.insert(PEOPLE_NUM_TABLE, null, values)
        db.close()
    }

    fun clearPeopletable() {
        val db = this.readableDatabase

        val clearDBQuery = "DELETE FROM $PEOPLE_NUM_TABLE"
        db.execSQL(clearDBQuery)
    }

    fun getAllFromPeopleNum(): Cursor? {
        val db = this.readableDatabase

        val queryGetAll = "SELECT * FROM ${PEOPLE_NUM_TABLE}"

        return db.rawQuery(queryGetAll, null)
    }



    companion object{
        private val DATABASE_NAME = "RestevrantkoDatabase"
        private val DATABASE_VERSION = 1

        val PEOPLE_NUM_TABLE = "People"
        val STEPS = "Steps"
        val CAR_NUM_TABLE = "Cars"

        val ID_COL = "id"

        val NUM_COL = "num"
        val LOCATION_COL = "location"
        val TIME_COL = "timeOfCapture" //format je YYYY-MM-DD HH:MM
        val USER_ID = "userId"

    }
}
