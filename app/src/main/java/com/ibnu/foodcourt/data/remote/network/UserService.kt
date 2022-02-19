package com.ibnu.foodcourt.data.remote.network

import com.ibnu.foodcourt.data.remote.request.LoginBody
import com.ibnu.foodcourt.data.remote.response.user.UserLoginResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserService {

    @POST("login")
    suspend fun loginUser(@Body request: LoginBody) : UserLoginResponse

}