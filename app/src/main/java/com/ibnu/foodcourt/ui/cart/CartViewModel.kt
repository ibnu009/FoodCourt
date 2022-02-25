package com.ibnu.foodcourt.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ibnu.foodcourt.data.model.Order
import com.ibnu.foodcourt.data.repository.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val orderRepository: OrderRepository
) : ViewModel() {

    fun getAllOrders(): LiveData<List<Order>> {
        val result = MutableLiveData<List<Order>>()
        viewModelScope.launch(Dispatchers.IO) {
            result.postValue(orderRepository.getAllOrder())
        }
        return result
    }

    fun removeOrder(order: Order) {
        viewModelScope.launch(Dispatchers.IO) {
            orderRepository.removeOrder(order)
        }
    }

    fun updateOrder(order: Order) {
        viewModelScope.launch(Dispatchers.IO) {
            orderRepository.updateOrder(order)
        }
    }

    fun clearOrder() {
        viewModelScope.launch(Dispatchers.IO) {
            orderRepository.clearOrder()
        }
    }

    fun getOrderItemTotal(): LiveData<Int> {
        val result = MutableLiveData<Int>()
        viewModelScope.launch(Dispatchers.IO) {
            result.postValue(orderRepository.getOrderItemTotal())
        }
        return result
    }

    fun getOrderTotalPrice(): LiveData<Int> {
        val result = MutableLiveData<Int>()
        viewModelScope.launch(Dispatchers.IO) {
            result.postValue(orderRepository.getOrderPriceTotal())
        }
        return result
    }


}