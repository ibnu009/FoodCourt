package com.ibnu.foodcourt.data.model

import com.google.gson.annotations.SerializedName

data class Product(
    val id: Int,

    @field:SerializedName("stand_id")
    val standId: Int,

    @field:SerializedName("category_id")
    val categoryId: Int,

    @field:SerializedName("product_name")
    val productName: String,

    val thumbnail: String,
    val description: String,
    val price: Int,

    @field:SerializedName("created_at")
    val createdAt: String,

    @field:SerializedName("updated_at")
    val updatedAt: String
)
