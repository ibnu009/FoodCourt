package com.ibnu.foodcourt.utils

interface PostStateHandler {
    fun onInitiating()
    fun onSuccess(message: String)
    fun onFailure(message: String)
}