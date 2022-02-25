package com.ibnu.foodcourt.data.repository

import com.ibnu.foodcourt.data.model.Order
import com.ibnu.foodcourt.data.remote.network.ApiResponse
import com.ibnu.foodcourt.data.remote.request.TransactionBody
import com.ibnu.foodcourt.data.source.OrderDataSource
import com.ibnu.foodcourt.data.source.ProductDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class OrderRepository @Inject constructor(
    private val orderDataSource: OrderDataSource,
    private val productDataSource: ProductDataSource
) {

    suspend fun getAllOrder(): List<Order> = orderDataSource.getAllOrder()

    suspend fun removeOrder(order: Order) = orderDataSource.removeOrder(order)

    suspend fun getOrderItemTotal() = orderDataSource.getOrderItemTotal()

    suspend fun getOrderPriceTotal() = orderDataSource.getOrderTotalPrice()

    suspend fun updateOrder(order: Order) = orderDataSource.updateOrder(order)

    suspend fun clearOrder() = orderDataSource.clearOrder()

    suspend fun postOrder(request: TransactionBody, token: String): Flow<ApiResponse<String>> =
        productDataSource.postTransaction(request, token)

}