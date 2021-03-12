package com.melowetty.investment

class StockItem(
    val title: String,
    val subtitle: String,
    val logo: String,
    val cost: Double,
    val difference: Double,
    private val growth: Boolean
)