package com.melowetty.investment.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavouriteStock(
    @PrimaryKey
    val ticker: String,
    val company: String)
