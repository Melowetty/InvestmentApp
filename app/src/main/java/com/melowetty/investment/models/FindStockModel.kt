package com.melowetty.investment.models

data class FindStockModel(
    val result: ArrayList<StockInfo>)
data class StockInfo(
    val symbol: String)
