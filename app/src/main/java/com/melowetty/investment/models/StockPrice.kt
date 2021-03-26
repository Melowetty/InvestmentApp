package com.melowetty.investment.models

import com.melowetty.investment.enums.Currency
import java.io.Serializable

data class StockPrice(
    val currency: Currency,
    val price: Double,
    val change: Double,
    val changePercent: Double,
    val up: Boolean): Serializable
