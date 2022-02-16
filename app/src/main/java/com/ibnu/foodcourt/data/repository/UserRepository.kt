package com.ibnu.foodcourt.data.repository

import com.ibnu.foodcourt.data.remote.network.ApiResponse
import com.ibnu.foodcourt.data.remote.request.LoginBody
import com.ibnu.foodcourt.data.remote.response.user.UserLoginResponse
import com.ibnu.foodcourt.data.source.UserDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userDataSource: UserDataSource,
) {

    suspend fun login(request: LoginBody): Flow<ApiResponse<UserLoginResponse>> {
        return userDataSource.loginUser(request).flowOn(Dispatchers.IO)
    }

}