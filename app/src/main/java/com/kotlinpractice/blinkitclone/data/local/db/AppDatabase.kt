package com.kotlinpractice.blinkitclone.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kotlinpractice.blinkitclone.data.local.dao.CategoryDao
import com.kotlinpractice.blinkitclone.data.local.entity.CategoryEntity

@Database(
    entities = [
        CategoryEntity::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
}
