package com.kotlinpractice.blinkitclone.di

import com.kotlinpractice.blinkitclone.data.local.dao.CategoryDao
import com.kotlinpractice.blinkitclone.data.local.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {

    @Provides
    fun provideCategoryDao(
        db: AppDatabase
    ): CategoryDao = db.categoryDao()
}
