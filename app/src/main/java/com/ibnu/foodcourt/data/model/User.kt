package com.ibnu.foodcourt.data.model

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

data class User(
    val id: Int,
    val name: String,
    val email: String,
    @field:SerializedName("email_verified_at")
    val emailVerifiedAt: JsonObject? = null,
    val role: String,
    @field:SerializedName("created_at")
    val createdAt: String,
    @field:SerializedName("updated_at")
    val updatedAt: String
)
