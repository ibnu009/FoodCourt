package com.ibnu.foodcourt.data.remote.network

import com.ibnu.foodcourt.data.remote.request.TransactionBody
import com.ibnu.foodcourt.data.remote.response.GenericResponse
import com.ibnu.foodcourt.data.remote.response.product.CategoriesResponse
import com.ibnu.foodcourt.data.remote.response.product.ProductResponse
import retrofit2.http.*

interface ProductService {

    @GET("product/{standId}/{categoryId}")
    suspend fun getProductList(
        @Header("Authorization") token: String,
        @Path("standId") standId: Int,
        @Path("categoryId") categoryId: Int): ProductResponse

    @GET("category")
    suspend fun getCategories(
        @Header("Authorization") token: String
    ): CategoriesResponse

    @POST("transaction")
    suspend fun postTransaction(
        @Header("Authorization") token: String,
        @Body request: TransactionBody
    ): GenericResponse
}