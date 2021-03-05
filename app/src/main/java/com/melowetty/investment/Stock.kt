package com.melowetty.investment

class Stock(
        val name: String,
        val surname: String,
        val icon: String,
        val cost: Double,
        private val currency: Currency
) {
    fun getCurrency(): String {
        return currency.toString()
    }
}