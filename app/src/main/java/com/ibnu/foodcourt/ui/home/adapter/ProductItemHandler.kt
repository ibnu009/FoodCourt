package com.ibnu.foodcourt.ui.home.adapter

import com.ibnu.foodcourt.data.model.Product

interface ProductItemHandler {
    fun addToCart(product: Product, price: Int)
}