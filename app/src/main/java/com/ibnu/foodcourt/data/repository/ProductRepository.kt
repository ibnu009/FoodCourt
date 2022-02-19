package com.ibnu.foodcourt.data.repository

import com.ibnu.foodcourt.data.model.Category
import com.ibnu.foodcourt.data.model.Product
import com.ibnu.foodcourt.data.remote.network.ApiResponse
import com.ibnu.foodcourt.data.source.ProductDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepository @Inject constructor(private val productDataSource: ProductDataSource) {

    suspend fun getProducts(standId: Int, categoryId: Int, token: String): Flow<ApiResponse<List<Product>>> {
        return productDataSource.getProductListFlow(standId, categoryId, token).flowOn(Dispatchers.IO)
    }

    suspend fun getCategories(token: String): Flow<ApiResponse<List<Category>>> {
        return productDataSource.getCategoriesFlow(token).flowOn(Dispatchers.IO)
    }


}