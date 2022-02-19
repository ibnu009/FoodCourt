package com.ibnu.foodcourt.data.remote.response.product

import com.google.gson.annotations.SerializedName
import com.ibnu.foodcourt.data.model.Category

data class CategoriesResponse(
    val message: String,
    @field:SerializedName("category")
    val categories: List<Category>,
    val status: Int
)
