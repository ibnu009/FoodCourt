package com.ibnu.foodcourt.data.remote.network

import com.ibnu.foodcourt.data.remote.request.LoginBody
import com.ibnu.foodcourt.data.remote.response.user.UserLoginResponse
import com.ibnu.foodcourt.data.remote.response.user.UserProfileResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface UserService {

    @POST("login")
    suspend fun loginUser(@Body request: LoginBody) : UserLoginResponse

    @POST("getuser")
    suspend fun getProfile(@Header("Authorization") token: String): UserProfileResponse

}