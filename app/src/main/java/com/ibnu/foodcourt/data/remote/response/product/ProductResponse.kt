package com.ibnu.foodcourt.data.remote.response.product

import com.ibnu.foodcourt.data.model.Product

data class ProductResponse(
    val message: String,
    val product: List<Product>,
    val status: Int
)
