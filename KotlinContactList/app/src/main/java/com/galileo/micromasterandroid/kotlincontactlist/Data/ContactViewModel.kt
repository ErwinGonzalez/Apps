package com.galileo.micromasterandroid.kotlincontactlist.Data

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import com.galileo.micromasterandroid.kotlincontactlist.DataModels.Contact

class ContactViewModel(application: Application) : AndroidViewModel(application) {
    private val contactRepository: ContactRepository
    private val contactData: LiveData<List<Contact>>

    init{
        contactRepository = ContactRepository(application)
        contactData = contactRepository.getAllContacts()
    }

    fun getAllContacts():LiveData<List<Contact>>{
        return contactData
    }


    fun insertContact(contact: Contact){
        contactRepository.insert(contact)
    }

    fun deleteAllContacts(){
        contactRepository.deleteAll()
    }
}