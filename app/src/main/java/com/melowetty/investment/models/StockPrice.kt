package com.melowetty.investment.models

import com.melowetty.investment.Currency

data class StockPrice(val cost: Float, val difference: Float, val differencePercent: Float, val up: Boolean, val currency: Currency)
