package com.ibnu.foodcourt.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "tbl_order")
data class Order(
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = false)
    val productId: Int,
    @field:SerializedName("stand_id")
    @ColumnInfo(name = "stand_id")
    val standId: Int,
    val productName: String,
    val thumbnail: String,
    val price: Int,
    var quantity: Int,
)