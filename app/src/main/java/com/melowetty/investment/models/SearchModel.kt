package com.melowetty.investment.models

data class SearchModel(val data: ArrayList<FindStock>)
data class FindStock(val symbol: String)
