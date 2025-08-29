package com.blake.sqldemo

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    var context: Context

    init { // save context parameter object for later use
        this.context = context
    }

    companion object {
        private val DB_NAME = "smt"
        private val DB_VERSION = 1
        val TABLE_NAME = "user_table"
        val ID = "id"
        val NAME = "name"
        val AGE = "age"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val query = (
                "CREATE TABLE $TABLE_NAME (" +
                        "$ID INTEGER PRIMARY KEY," +
                        "$NAME TEXT," +
                        "$AGE TEXT" + ")"
                )
        db?.execSQL(query) // nullable
    }

    // Called when the database needs to be upgraded
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME") // non-null assertion.
        //Error if null at compile time
        onCreate(db)
    }

    // This method is to add a User record in DB
    fun addUser(name: String, age: String) {
        if (name == "" || age == "") {
            return
        }
        // This ContentValues class is used to store a set of values
        val values = ContentValues()
        // insert key-value pairs
        values.put(NAME, name.trimEnd())
        values.put(AGE, age.trimEnd())
        // create a writable DB variable of our database to insert record
        val db = this.writableDatabase
        // insert all values into DB
        db.insert(TABLE_NAME, null, values)
        // close DB
        db.close()
    }

    // This method is get all User records from DB
    fun getAllUsers(): ArrayList<User> {
        // create a readable DB variable of our database to read record
        val db = this.readableDatabase
        // read all records from DB and get the cursor
        val cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null)
        val userList = ArrayList<User>() // User ArrayList

        if (cursor.moveToFirst()) {
            do { // add all users to the list
                userList.add(
                    User(
                        cursor.getInt(cursor.getColumnIndexOrThrow(ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(AGE))
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return userList
    }

    fun deleteUser(name: String): Int {
        var rows = 0
        // Use the actual 'name' parameter, not DB_NAME
        try {
            val db = this.writableDatabase
            rows = db.delete(
                TABLE_NAME,
                "LOWER(name) = LOWER(?)",
                arrayOf(name.trim())
            )
            db.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return rows
    }

    fun updateUser(name: String, age: String): Int {
        // create a writable DB variable of our database to update record
        val db = this.writableDatabase
        // This ContentValues class is used to store a set of values
        val values = ContentValues()
        values.put(AGE, age)
        val rows = db.update(TABLE_NAME, values, "name=?", arrayOf(name))
        db.close()
        return rows // rows updated
    }

    // This method is to recreated DB and tables
    fun recreateDatabaseAndTables() {
    }

    fun deleteDB(): Boolean {
        return context.deleteDatabase(DB_NAME)
    }

    fun printByAge(age: String): ArrayList<User> {
        // create a readable DB variable of our database to read record
        val db = this.readableDatabase
        val userList = ArrayList<User>() // User ArrayList
        // read all records from DB and get the cursor
        val cursor = db.rawQuery(" SELECT * FROM " + TABLE_NAME + " WHERE $AGE = ? ", arrayOf(age))

        if (cursor.moveToFirst()) {
            do {
                val user = User(
                cursor.getInt(cursor.getColumnIndexOrThrow(ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(AGE)))
                userList.add(user)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return userList
    }
}