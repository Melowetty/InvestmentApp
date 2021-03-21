package com.melowetty.investment.models

import com.melowetty.investment.Currency

data class StockPrice(val currency: Currency, val price: Double, val change: Double, val changePercent: Double, val up: Boolean)
