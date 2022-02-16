package com.ibnu.foodcourt.data.remote.response.user

import com.google.gson.annotations.SerializedName
import com.ibnu.foodcourt.data.model.User

data class UserLoginResponse(
    val message: String,
    val user: User,
    val token: String,
    @field:SerializedName("token_type")
    val tokenType: String,
    val status: Int

)
