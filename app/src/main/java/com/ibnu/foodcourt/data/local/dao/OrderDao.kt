package com.ibnu.foodcourt.data.local.dao

import androidx.room.*
import com.ibnu.foodcourt.data.model.Order
import retrofit2.http.DELETE

@Dao
interface OrderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addOrderToCart(order: Order)

    @Query("SELECT * FROM tbl_order")
    suspend fun getAllOrders(): List<Order>


    @Query("UPDATE tbl_order SET quantity =:quantity WHERE id = :productId")
    suspend fun updateOrderQuantity(quantity: Int, productId: Int)

    @Update
    suspend fun updateOrder(order: Order)

    @Query("SELECT EXISTS(SELECT * FROM tbl_order WHERE id = :productId)")
    suspend fun isProductExist(productId: Int): Boolean

    @Query("SELECT quantity FROM tbl_order WHERE id = :productId")
    suspend fun productQuantity(productId: Int): Int

    @Delete
    suspend fun removeOrderFromCart(order: Order)

    @Query("DELETE FROM tbl_order")
    suspend fun removeAllItem()
}