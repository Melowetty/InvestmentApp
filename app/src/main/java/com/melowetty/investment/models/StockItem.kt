package com.melowetty.investment.models

data class StockItem(
    val title: String,
    val subtitle: String?,
    val logo: String?,
    val priceInfo: StockPrice
)