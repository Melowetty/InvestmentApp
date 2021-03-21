package com.melowetty.investment.models

data class CompanyInfoModel(val quoteSummary : QuoteSummary)
data class QuoteSummary(val result : List<PriceResult>)
data class PriceResult(val price : PriceInfo)
data class PriceInfo(
    val regularMarketChangePercent : RegularMarketChangePercent,
    val regularMarketPrice : RegularMarketPrice,
    val regularMarketChange : RegularMarketChange,
    val currency : String,
    val shortName: String,
    val symbol: String)
data class RegularMarketChange(val fmt: Double)
data class RegularMarketChangePercent(val fmt : String)
data class RegularMarketPrice(val fmt : Double)