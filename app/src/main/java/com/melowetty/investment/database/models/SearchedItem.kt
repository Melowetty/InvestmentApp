package com.melowetty.investment.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SearchedItem(
    @PrimaryKey
    val ticker: String
)