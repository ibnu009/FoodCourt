package com.ibnu.foodcourt.utils.ext

import java.text.DecimalFormat

fun Int.toRupiah(): String{
    val formatter = DecimalFormat("#,###")
    val rawFormattedPrice = formatter.format(this)
    val formattedPrice = rawFormattedPrice.replace(",",".")

    return "Rp $formattedPrice"
}