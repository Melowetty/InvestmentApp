package com.melowetty.investment.models

import java.io.Serializable

data class Stock(
    val symbol: String,
    val company: String,
    val logo: String,
    val stockPrice: StockPrice): Serializable
