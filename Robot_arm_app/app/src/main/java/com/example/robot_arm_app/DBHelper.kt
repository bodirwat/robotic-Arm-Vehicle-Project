package com.example.robot_arm_app
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

//creating the database logic, extending the SQLiteOpenHelper base class
class DDHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "robotDatabase"

        private const val TABLE_CONTACTS = "robotTable"

        private const val KEY_ID = "_id"
        private const val KEY_NAMEofLOCATION = "name"
        private const val KEY_NAMEofITEM = "item"
        private const val KEY_NUMBERofITENS = "numItems"
        private const val KEY_DATE = "dayTIME"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        //creating table with fields
        val CREATE_CONTACTS_TABLE = ("CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAMEofLOCATION + " TEXT,"
                + KEY_NAMEofITEM + " TEXT,"
                + KEY_DATE + " TEXT,"
                + KEY_NUMBERofITENS + " TEXT" + ")")
        db?.execSQL(CREATE_CONTACTS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_CONTACTS")
        onCreate(db)
    }


    /**
     * Function to insert data
     */
    fun addEmployee(emp: EmpModelClass): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(KEY_NAMEofLOCATION, emp.name) // EmpModelClass Name
        contentValues.put(KEY_NAMEofITEM, emp.nameITM) // EmpModelClass item name
        contentValues.put(KEY_NUMBERofITENS, emp.NumITM) // EmpModelClass number of iems
        contentValues.put(KEY_DATE, emp.rDate) // EmpModelClass number of iems

        // Inserting employee details using insert query.
        val success = db.insert(TABLE_CONTACTS, null, contentValues)
        //2nd argument is String containing nullColumnHack

        db.close() // Closing database connection
        return success
    }



    //Method to read the records from database in form of ArrayList
    @SuppressLint("Range")
    fun viewEmployee(): ArrayList<EmpModelClass> {

        val empList: ArrayList<EmpModelClass> = ArrayList<EmpModelClass>()

        // Query to select all the records from the table.
        val selectQuery = "SELECT  * FROM $TABLE_CONTACTS"

        val db = this.readableDatabase
        // Cursor is used to read the record one by one. Add them to data model class.
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(selectQuery, null)

        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var nameLOCATION: String
        var nameITMA: String

        var dayTA:String
        var numITMA: String

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                nameLOCATION = cursor.getString(cursor.getColumnIndex(KEY_NAMEofLOCATION))
                nameITMA = cursor.getString(cursor.getColumnIndex(KEY_NAMEofITEM))
                numITMA = cursor.getString(cursor.getColumnIndex(KEY_NUMBERofITENS))
                dayTA = cursor.getString(cursor.getColumnIndex(KEY_DATE))


                val emp = EmpModelClass(id = id, name = nameLOCATION,  nameITM = nameITMA , NumITM = numITMA , rDate = dayTA)
                empList.add(emp)

            } while (cursor.moveToNext())
        }
        return empList
    }



    /**
     * Function to update record
     */
    fun updateEmployee(emp: EmpModelClass): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_NAMEofLOCATION, emp.name) // EmpModelClass Name
        contentValues.put(KEY_NAMEofITEM, emp.nameITM) // EmpModelClass Email
        contentValues.put(KEY_NUMBERofITENS, emp.NumITM) // EmpModelClass Email
        contentValues.put(KEY_DATE, emp.rDate) // EmpModelClass Email

        // Updating Row
        val success = db.update(TABLE_CONTACTS, contentValues, KEY_ID + "=" + emp.id, null)
        //2nd argument is String containing nullColumnHack

        // Closing database connection
        db.close()
        return success
    }


    /**
     * Function to delete record
     */
    fun deleteEmployee(emp: EmpModelClass): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, emp.id) // EmpModelClass id
        // Deleting Row
        val success = db.delete(TABLE_CONTACTS, KEY_ID + "=" + emp.id, null)
        //2nd argument is String containing nullColumnHack

        // Closing database connection
        db.close()
        return success
    }

}