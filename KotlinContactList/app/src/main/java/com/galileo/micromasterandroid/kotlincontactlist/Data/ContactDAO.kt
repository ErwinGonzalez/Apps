package com.galileo.micromasterandroid.kotlincontactlist.Data

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.galileo.micromasterandroid.kotlincontactlist.DataModels.Contact

@Dao //1
interface ContactDAO{
    @Query ("SELECT * FROM contactList")//2
    fun getAll(): LiveData<List<Contact>>//3

    @Insert(onConflict = OnConflictStrategy.REPLACE)//4
    fun insert(contactList : Contact)

    @Query("DELETE FROM contactList")
    fun deleteAll()
}