package com.ibnu.foodcourt.data.source

import com.ibnu.foodcourt.data.model.Category
import com.ibnu.foodcourt.data.model.Product
import com.ibnu.foodcourt.data.remote.network.ApiResponse
import com.ibnu.foodcourt.data.remote.network.ProductService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.net.HttpURLConnection
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductDataSource @Inject constructor(private val productService: ProductService){

    suspend fun getProductListFlow(standId: Int, categoryId: Int, token: String): Flow<ApiResponse<List<Product>>> {
        return flow {
            try {
                emit(ApiResponse.Loading)
                val response = productService.getProductList(token, standId, categoryId)
                if (response.status == HttpURLConnection.HTTP_OK){
                    if (response.product.isEmpty()){
                        emit(ApiResponse.Empty)
                    } else {
                        emit(ApiResponse.Success(response.product))
                    }
                } else {
                    emit(ApiResponse.Error(response.message))
                }
            } catch (e: Exception){
                emit(ApiResponse.Error(e.message.toString()))
            }
        }
    }

    suspend fun getCategoriesFlow(token: String): Flow<ApiResponse<List<Category>>> {
        return flow {
            try {
                emit(ApiResponse.Loading)
                val response = productService.getCategories(token)
                if (response.status == HttpURLConnection.HTTP_OK){
                    emit(ApiResponse.Success(response.categories))
                } else {
                    emit(ApiResponse.Error(response.message))
                }
            } catch (e: Exception){
                emit(ApiResponse.Error(e.message.toString()))
            }
        }
    }

}