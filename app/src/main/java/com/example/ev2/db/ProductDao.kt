package com.example.ev2.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ProductDao {

    @Query("SELECT * FROM product")
    fun findAll(): List<Product>

    @Delete
    fun delete(product:Product)

    @Insert
    fun insert(product:Product):Long

    @Update
    fun update(product: Product)
}