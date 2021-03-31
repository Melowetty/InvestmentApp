package com.melowetty.investment.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_stocks")
data class FavoriteStock(
    @PrimaryKey
    val ticker: String,
    val company: String)
