package com.ibnu.foodcourt.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ibnu.foodcourt.data.local.dao.OrderDao
import com.ibnu.foodcourt.data.local.dao.ProductDao
import com.ibnu.foodcourt.data.model.Order
import com.ibnu.foodcourt.data.model.Product

@Database(
    entities = [Product::class, Order::class],
    version = 1,
    exportSchema = false
)
abstract class FoodCourtDatabase : RoomDatabase() {

    abstract fun getProductDao(): ProductDao
    abstract fun getOrderDao(): OrderDao

}