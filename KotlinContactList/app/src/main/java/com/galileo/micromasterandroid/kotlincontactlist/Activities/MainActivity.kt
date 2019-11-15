package com.galileo.micromasterandroid.kotlincontactlist.Activities

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.galileo.micromasterandroid.kotlincontactlist.Adapters.ContactRecyclerViewAdapter
import com.galileo.micromasterandroid.kotlincontactlist.Data.ContactViewModel
import com.galileo.micromasterandroid.kotlincontactlist.DataModels.Contact
import com.galileo.micromasterandroid.kotlincontactlist.R
import com.galileo.micromasterandroid.kotlincontactlist.Workers.NotificationWorker
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ContactRecyclerViewAdapter.OnClickCallback {

    val NEW_CONTACT_REQUEST = 1001
    var contactViewModel: ContactViewModel?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        contactViewModel = ViewModelProviders.of(this).get(ContactViewModel::class.java)

        val adapter = ContactRecyclerViewAdapter(this,this)
        val recyclerView = findViewById<RecyclerView>(R.id.contact_list_rv)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        contactViewModel?.getAllContacts()?.observe(this, object: Observer<List<Contact>>{
            override fun onChanged(t: List<Contact>?) {
                adapter.setList(t!!)
            }
        })

        fab.setOnClickListener { view ->
            val intent = Intent(this, AddContactActivity::class.java)
            startActivityForResult(intent,NEW_CONTACT_REQUEST)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == NEW_CONTACT_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val bundle = data.getBundleExtra("RESULT_BUNDLE")
            val contact = bundle.get("NEW_CONTACT")
            contactViewModel?.insertContact(contact as Contact)
        }

    }

    override fun handleClick(position: Int) {
        val selectedContact = contactViewModel?.getAllContacts()?.value?.get(position) as Contact
        //Toast.makeText(this,selectedContact.toString(),Toast.LENGTH_SHORT).show()
        val data:Data = Data.Builder()
                .putString(NotificationWorker.EXTRA_NAME,selectedContact.name)
                .putString(NotificationWorker.EXTRA_PHONE,selectedContact.phone)
                .build()
        val singleTimeNotification: OneTimeWorkRequest = OneTimeWorkRequest.Builder(NotificationWorker::class.java)
                                                            .setInputData(data)
                                                            .build()
        WorkManager.getInstance().enqueue(singleTimeNotification)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this,SettingsActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
