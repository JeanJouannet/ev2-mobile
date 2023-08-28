package com.example.ev2.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Product (

    @PrimaryKey(autoGenerate = true)
    val uid:Int,

    @ColumnInfo
    var name:String,

    @ColumnInfo
    var bought:Boolean
)