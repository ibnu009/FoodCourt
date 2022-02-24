package com.ibnu.foodcourt.di

import android.content.Context
import androidx.room.Room
import com.ibnu.foodcourt.data.local.dao.OrderDao
import com.ibnu.foodcourt.data.local.dao.ProductDao
import com.ibnu.foodcourt.data.local.room.FoodCourtDatabase
import com.ibnu.foodcourt.utils.ConstVal.DB_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): FoodCourtDatabase {
        return Room.databaseBuilder(
            context,
            FoodCourtDatabase::class.java, DB_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideProductDao(database: FoodCourtDatabase): ProductDao = database.getProductDao()

    @Provides
    fun provideOrderDao(database: FoodCourtDatabase): OrderDao = database.getOrderDao()

}