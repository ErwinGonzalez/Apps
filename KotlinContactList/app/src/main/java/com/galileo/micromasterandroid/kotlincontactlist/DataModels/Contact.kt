package com.galileo.micromasterandroid.kotlincontactlist.DataModels

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "contactList")//1
data class Contact (@PrimaryKey(autoGenerate = true) var id:Long?,//2
                    @ColumnInfo(name = "name") var name:String,//3
                    @ColumnInfo(name = "phone") var phone:String) : Serializable
{
    constructor():this(null,"","")//4
}

