package com.ibnu.foodcourt.data.source

import com.ibnu.foodcourt.data.local.dao.OrderDao
import com.ibnu.foodcourt.data.model.Order
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderDataSource @Inject constructor(
    private val orderDao: OrderDao,
) {

    suspend fun getAllOrder(): List<Order> = orderDao.getAllOrders()

    suspend fun removeOrder(order: Order) = orderDao.removeOrderFromCart(order)

    suspend fun getOrderItemTotal(): Int {
        val orders = orderDao.getAllOrders()
        var itemTotal = 0
        for (order in orders) {
            itemTotal += order.quantity
        }

        return itemTotal
    }

    suspend fun getOrderTotalPrice(): Int {
        val orders = orderDao.getAllOrders()
        var totalPrice = 0
        for (order in orders) {
            totalPrice += (order.price * order.quantity)
        }
        return totalPrice
    }

    suspend fun clearOrder() {
        orderDao.removeAllItem()
    }

    suspend fun updateOrder(order: Order) {
        orderDao.updateOrder(order)
    }

}