package com.ibnu.foodcourt.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ibnu.foodcourt.data.remote.network.ApiResponse
import com.ibnu.foodcourt.data.remote.request.LoginBody
import com.ibnu.foodcourt.data.remote.response.user.UserLoginResponse
import com.ibnu.foodcourt.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    fun login(loginBody: LoginBody): LiveData<ApiResponse<UserLoginResponse>> {
        val result = MutableLiveData<ApiResponse<UserLoginResponse>>()
        viewModelScope.launch {
            userRepository.login(loginBody).collect {
                result.postValue(it)
            }
        }
        return result
    }

}