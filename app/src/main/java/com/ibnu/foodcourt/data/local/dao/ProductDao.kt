package com.ibnu.foodcourt.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ibnu.foodcourt.data.model.Product

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllProducts(products: List<Product>)

    @Query("SELECT * FROM product")
    suspend fun getAllProducts(): List<Product>

    @Query("SELECT * FROM product WHERE category_id = :categoryId")
    suspend fun getAllProductsByCategory(categoryId: Int): List<Product>

    @Query("UPDATE product SET quantity = :quantity WHERE id = :id")
    suspend fun updateProductQuantity(id: Int, quantity: Int)
}