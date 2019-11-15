package com.galileo.micromasterandroid.kotlincontactlist.Activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import com.galileo.micromasterandroid.kotlincontactlist.DataModels.Contact
import com.galileo.micromasterandroid.kotlincontactlist.R
import kotlinx.android.synthetic.main.activity_main.*

class AddContactActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_contact_activity)
        setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.add_contact_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_ok -> {
                val nameTil = findViewById<TextInputLayout>(R.id.name_til)
                val nameTv = findViewById<TextInputEditText>(R.id.name_edit_text)
                val phoneTil = findViewById<TextInputLayout>(R.id.phone_til)
                val phoneTv = findViewById<TextInputEditText>(R.id.phone_edit_text)
                var empty = false;
                if(TextUtils.isEmpty(nameTv?.text.toString())) {
                    nameTil?.error ="Name cannot be empty";
                    empty = true
                }
                if(TextUtils.isEmpty(phoneTv?.text.toString())) {
                    phoneTil?.error ="Phone cannot be empty";
                    empty = true
                }
                if(!empty){
                    val newContact = Contact(null,
                        nameTv.text.toString(),
                        phoneTv.text.toString()
                    )
                    val bundle = Bundle()
                    bundle.putSerializable("NEW_CONTACT",newContact)
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("RESULT_BUNDLE",bundle)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
                return true;
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}