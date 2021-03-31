package com.melowetty.investment.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search_history")
data class FoundTicker(
    @PrimaryKey
    val ticker: String)