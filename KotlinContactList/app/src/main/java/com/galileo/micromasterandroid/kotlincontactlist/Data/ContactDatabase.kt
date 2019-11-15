package com.galileo.micromasterandroid.kotlincontactlist.Data

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.galileo.micromasterandroid.kotlincontactlist.DataModels.Contact

@Database(entities = arrayOf(Contact::class), version = 1)//1

abstract class ContactDatabase : RoomDatabase(){
    abstract fun contactDAO(): ContactDAO//2

    companion object {//3
        private var INSTANCE: ContactDatabase? = null

        fun getInstance(context: Context): ContactDatabase?{
            if(INSTANCE == null){
                synchronized(ContactDatabase::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                                ContactDatabase::class.java,
                            "contacts.db")
                        .build()
                }
            }
            return INSTANCE
        }
        fun destroyInstance(){
            INSTANCE = null
        }
    }
}