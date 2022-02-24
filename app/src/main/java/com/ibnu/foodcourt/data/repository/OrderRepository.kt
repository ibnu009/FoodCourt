package com.ibnu.foodcourt.data.repository

import com.ibnu.foodcourt.data.model.Order
import com.ibnu.foodcourt.data.source.OrderDataSource
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class OrderRepository @Inject constructor(
    private val orderDataSource: OrderDataSource
) {

    suspend fun getAllOrder(): List<Order> = orderDataSource.getAllOrder()

    suspend fun getOrderItemTotal() = orderDataSource.getOrderItemTotal()

    suspend fun getOrderPriceTotal() = orderDataSource.getOrderTotalPrice()



}