package com.melowetty.investment.models

data class ExchangeRateModel(
    val quote: Exchanges)
data class Exchanges(
    val EUR: Double,
    val RUB: Double)
