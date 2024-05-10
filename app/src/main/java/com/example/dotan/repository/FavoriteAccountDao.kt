package com.example.dotan.repository

import androidx.room.*
import com.example.dotan.FavoriteAccount
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteAccountDao {

    @Query("SELECT * FROM favorite_accounts")
    fun getAllFavoriteAccounts(): Flow<List<FavoriteAccount>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteAccount(account: FavoriteAccount)

    @Delete
    suspend fun deleteFavoriteAccount(account: FavoriteAccount)
}