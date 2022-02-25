package com.ibnu.foodcourt.data.remote.request

import com.google.gson.annotations.SerializedName

data class Transaction(
    @field:SerializedName("product_id")
    val productId: Int,
    @field:SerializedName("stand_id")
    val standId: Int,
    val table: Int,
    @field:SerializedName("qty")
    val quantity: Int,
    val status: Int,
    @field:SerializedName("buyer_name")
    val buyerName: String
)
