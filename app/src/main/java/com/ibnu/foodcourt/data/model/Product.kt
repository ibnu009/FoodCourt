package com.ibnu.foodcourt.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "product")
data class Product(
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    @field:SerializedName("stand_id")
    @ColumnInfo(name = "stand_id")
    val standId: Int,
    @field:SerializedName("category_id")
    @ColumnInfo(name = "category_id")
    val categoryId: Int,
    @field:SerializedName("product_name")
    @ColumnInfo(name = "product_name")
    val productName: String,
    val thumbnail: String,
    val description: String,
    val price: Int,
    @field:SerializedName("created_at")
    @ColumnInfo(name = "created_at")
    val createdAt: String,
    @field:SerializedName("updated_at")
    @ColumnInfo(name = "updated_at")
    val updatedAt: String,
    var quantity: Int = 0
)
