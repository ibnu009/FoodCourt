package com.ibnu.foodcourt.data.model

import com.google.gson.annotations.SerializedName

data class Category(
    val id: Int,

    @field:SerializedName("category_name")
    val categoryName: String,

    @field:SerializedName("created_at")
    val createdAt: String,

    @field:SerializedName("updated_at")
    val updatedAt: String
)