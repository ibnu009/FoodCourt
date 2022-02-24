package com.ibnu.foodcourt.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ibnu.foodcourt.data.model.Category
import com.ibnu.foodcourt.data.model.Product
import com.ibnu.foodcourt.data.remote.network.ApiResponse
import com.ibnu.foodcourt.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    fun getProducts(standId: Int, categoryId: Int, token: String): LiveData<ApiResponse<List<Product>>> {
        val result = MutableLiveData<ApiResponse<List<Product>>>()
        viewModelScope.launch {
            productRepository.getProducts(standId, categoryId, token).collect {
                result.postValue(it)
            }
        }
        return result
    }

    fun getCategories(token: String): LiveData<ApiResponse<List<Category>>> {
        val result = MutableLiveData<ApiResponse<List<Category>>>()
        viewModelScope.launch {
            productRepository.getCategories(token).collect {
                result.postValue(it)
            }
        }
        return result
    }

    fun insertProductToCart(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            productRepository.insertToCart(product)
        }
    }

    fun getOrderItemTotal() : LiveData<Int> {
        val result = MutableLiveData<Int>()
        viewModelScope.launch(Dispatchers.IO) {
            result.postValue(productRepository.getOrderItemTotal())
        }
        return result
    }

    fun getOrderTotalPrice(): LiveData<Int> {
        val result = MutableLiveData<Int>()
        viewModelScope.launch(Dispatchers.IO) {
            result.postValue(productRepository.getOrderPriceTotal())
        }
        return result
    }

}