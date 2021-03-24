package com.melowetty.investment.models

data class Stock(
    val symbol: String,
    val company: String,
    val logo: String,
    val stockPrice: StockPrice)
