package com.melowetty.investment.models

data class FindStockModel(val data: ArrayList<StockInfo>)
data class StockInfo(val symbol: String)
