package com.galileo.micromasterandroid.kotlincontactlist.Activities

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.Toast
import com.galileo.micromasterandroid.kotlincontactlist.Data.ContactViewModel
import com.galileo.micromasterandroid.kotlincontactlist.R

class SettingsActivity : AppCompatActivity() {

    var contactViewModel: ContactViewModel?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_layout)
        val deleteButton = findViewById<Button>(R.id.delete_data_button)
        contactViewModel = ViewModelProviders.of(this).get(ContactViewModel::class.java)
        deleteButton.setOnClickListener{ view ->
           contactViewModel?.deleteAllContacts()
            Toast.makeText(this,"All Contacts Deleted",Toast.LENGTH_SHORT).show()
        }
    }
}