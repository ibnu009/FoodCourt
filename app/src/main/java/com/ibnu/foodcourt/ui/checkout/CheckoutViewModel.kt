package com.ibnu.foodcourt.ui.checkout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ibnu.foodcourt.data.model.Order
import com.ibnu.foodcourt.data.remote.network.ApiResponse
import com.ibnu.foodcourt.data.remote.request.TransactionBody
import com.ibnu.foodcourt.data.repository.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val orderRepository: OrderRepository
) : ViewModel() {

    fun getAllOrders(): LiveData<List<Order>> {
        val result = MutableLiveData<List<Order>>()
        viewModelScope.launch(Dispatchers.IO) {
            result.postValue(orderRepository.getAllOrder())
        }
        return result
    }

    fun clearOrder() {
        viewModelScope.launch(Dispatchers.IO) {
            orderRepository.clearOrder()
        }
    }

    fun postTransaction(request: TransactionBody, token: String): LiveData<ApiResponse<String>> {
        val result = MutableLiveData<ApiResponse<String>>()
        viewModelScope.launch {
            orderRepository.postOrder(request, token).collect {
                result.postValue(it)
            }
        }
        return result
    }


}