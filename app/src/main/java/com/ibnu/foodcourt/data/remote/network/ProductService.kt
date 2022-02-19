package com.ibnu.foodcourt.data.remote.network

import com.ibnu.foodcourt.data.remote.response.product.CategoriesResponse
import com.ibnu.foodcourt.data.remote.response.product.ProductResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

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
}