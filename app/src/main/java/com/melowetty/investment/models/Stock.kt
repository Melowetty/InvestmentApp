package com.melowetty.investment.models

data class Stock(
    val symbol: String,
    val company: String,
    val stockPrice: StockPrice)
