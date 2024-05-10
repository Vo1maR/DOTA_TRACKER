package com.example.dotan.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.dotan.FavoriteAccount

@Database(entities = [FavoriteAccount::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteAccountDao(): FavoriteAccountDao
}