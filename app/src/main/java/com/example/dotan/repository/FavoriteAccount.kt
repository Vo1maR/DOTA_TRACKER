package com.example.dotan.repository

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_accounts")
data class FavoriteAccount(
    @PrimaryKey val account_id: Int,
    val personaname: String?,
    val avatar: String?
)