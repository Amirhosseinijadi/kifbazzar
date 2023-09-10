package com.example.model.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.model.data.Product

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
      fun insertorupdate(products : List<Product>)

    @Query("SELECT * FROM product_table")
     fun getall():List<Product>

    @Query("SELECT * FROM product_table WHERE productId = :productId")
      fun getProductbyID(productId:String):Product

    @Query("SELECT * FROM product_table WHERE category = :category")
     fun getallByCategory(category: String): List<Product>


}