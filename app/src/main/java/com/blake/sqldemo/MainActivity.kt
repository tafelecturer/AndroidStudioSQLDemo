package com.blake.sqldemo

import android.content.DialogInterface
import android.os.Bundle
import android.service.autofill.Validators.or
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val btnAddUser = findViewById<Button>(R.id.btnAddUser)

        btnAddUser.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(this)
            val inflater = this.layoutInflater
            val dialogView = inflater.inflate(R.layout.add_dialog, null)
            dialogBuilder.setTitle("Add New User")
            dialogBuilder.setMessage("Enter data below")
            dialogBuilder.setView(dialogView)
            // Set a listener to each button that takes an action before dismissing the dialog
            // The dialog is automatically dismissed when a dialog button is clicked
            dialogBuilder.setPositiveButton("Yes",
                DialogInterface.OnClickListener { dialog, id ->
                    // do this if "Yes" is clicked
                    val db = DBHelper(this, null)
                    val etAddName = dialogView.findViewById<EditText>(R.id.etAddName)
                    val etAddAge = dialogView.findViewById<EditText>(R.id.etAddAge)
                    val name = etAddName.text.toString()
                    val age = etAddAge.text.toString()
                    //null check message
                    if (name == "" || age == "")
                    {
                        Toast.makeText(this, "username or age cannot be blank here", Toast.LENGTH_SHORT).show()
                    }

                    else
                    {
                        db.addUser(name, age)
                        Toast.makeText(this, "$name added to database", Toast.LENGTH_SHORT).show()
                    }
                }
            )
            dialogBuilder.setNegativeButton("No",
                DialogInterface.OnClickListener { dialog, id ->
                    // do this if "No" is clicked
                    // Nothing is performed, so you can put null instead
                })
            dialogBuilder.setIcon(android.R.drawable.ic_dialog_alert)
            dialogBuilder.show()
        }

        val btnPrintUsers = findViewById<Button>(R.id.btnPrintUsers)
        btnPrintUsers.setOnClickListener {
            val db = DBHelper(this, null)
            val userList = db.getAllUsers()
            val tvUserRecord = findViewById<TextView>(R.id.tvUserRecord)
            tvUserRecord.text = "### Users ###\n"
            userList.forEach { tvUserRecord.append("$it\n") }
        }

        val btnDeleteUser = findViewById<Button>(R.id.btnDeleteUser)
        btnDeleteUser.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(this)
            val dialogView = this.layoutInflater.inflate(R.layout.delete_dialog, null)
            dialogBuilder.setTitle("Delete User")
            dialogBuilder.setMessage("Enter data below")
            dialogBuilder.setView(dialogView)
            // Set a listener to each button that takes an action before dismissing the dialog
            // The dialog is automatically dismissed when a dialog button is clicked
            dialogBuilder.setPositiveButton("Yes",
                DialogInterface.OnClickListener { dialog, id ->
                    // do this if "Yes" is clicked
                    val db = DBHelper(this, null)
                    val etDeleteName = dialogView.findViewById<EditText>(R.id.etDeleteName)
                    val name = etDeleteName.text.toString()
                    val rows = db.deleteUser(name)
                    Toast.makeText(
                        this,
                        when (rows) {
                            0 -> "Nothing deleted"
                            1 -> "1 user deleted"
                            else -> "" // shouldn't happen
                        },
                        Toast.LENGTH_LONG
                    ).show()
                }
            )
            dialogBuilder.setNegativeButton("No", null) // nothing to do
            dialogBuilder.setIcon(android.R.drawable.ic_dialog_alert)
            dialogBuilder.show()
        }

        val btnUpdateUser = findViewById<Button>(R.id.btnUpdateUser)
        btnUpdateUser.setOnClickListener {
            val dialogView = this.layoutInflater.inflate(R.layout.update_dialog, null)
            /*
            Kotlin is both a functional programming and an OOP language
            */
            AlertDialog.Builder(this)
                .setTitle("Update User")
                .setMessage("Enter data below")
                .setView(dialogView)
                // Set a listener to each button that takes an action before dismissing the dialog
                // The dialog is automatically dismissed when a dialog button is clicked
                .setPositiveButton(
                    "Yes",
            DialogInterface.OnClickListener { dialog, id ->
                // do this if "Yes" is clicked
                val db = DBHelper(this, null)
                val etUpdateName = dialogView.findViewById<EditText>(R.id.etUpdateName)
                val etUpdateAge = dialogView.findViewById<EditText>(R.id.etUpdateAge)
                val name = etUpdateName.text.toString()
                val age = etUpdateAge.text.toString()
                val rows = db.updateUser(name, age)
                Toast.makeText(this, "$rows users updated", Toast.LENGTH_LONG).show()
            }
            )
            .setNegativeButton("No", null) // nothing to do
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()
        }

        val btnDeleteDB = findViewById<Button>(R.id.btnDeleteDB)
        btnDeleteDB.setOnClickListener {
            val db = DBHelper(this, null)
            val isSuccessful = db.deleteDB()
            Toast.makeText(this,
                when (isSuccessful) {
                    true -> "Database successfully deleted"
                    false -> "Failed to delete database"
                },
                Toast.LENGTH_LONG
            ).show()
        }
    }
}