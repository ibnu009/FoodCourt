package com.ibnu.foodcourt.data.source

import com.ibnu.foodcourt.data.model.User
import com.ibnu.foodcourt.data.remote.network.ApiResponse
import com.ibnu.foodcourt.data.remote.network.UserService
import com.ibnu.foodcourt.data.remote.request.LoginBody
import com.ibnu.foodcourt.data.remote.response.user.UserLoginResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.net.HttpURLConnection
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserDataSource @Inject constructor(private val userService: UserService){

    suspend fun loginUser(request: LoginBody): Flow<ApiResponse<UserLoginResponse>> {
        return flow {
            try {
                emit(ApiResponse.Loading)
                val response = userService.loginUser(request)
                if (response.status == HttpURLConnection.HTTP_OK) {
                    emit(ApiResponse.Success(response))
                } else {
                    emit(ApiResponse.Error(response.message ?: "Gagal"))
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.message.toString()))
            }
        }
    }

    suspend fun getUserProfile(token: String): Flow<ApiResponse<User>> {
        return flow {
            try {
                emit(ApiResponse.Loading)
                val response = userService.getProfile(token)
                emit(ApiResponse.Success(response.data))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.message.toString()))
            }
        }
    }
}