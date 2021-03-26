package com.melowetty.investment.listeners

import com.melowetty.investment.models.Stock

interface StockClickListener {
    fun onStockClick(stock: Stock)
}