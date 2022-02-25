package com.ibnu.foodcourt.ui.cart.adapter

import com.ibnu.foodcourt.data.model.Order

interface OrderItemHandler {
    fun onIncrementOrderQuantity(order: Order, position: Int)
    fun onDecrementOrderQuantity(order: Order, position: Int)
    fun onDeleteOrder(order: Order, position: Int)
}