package com.melowetty.investment.models

import java.io.Serializable

data class Stock(
    val symbol: String,
    val company: String,
    val logo: String,
    var isFavourite: Boolean,
    var stockPrice: StockPrice): Serializable
