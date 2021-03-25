package com.melowetty.investment

import com.melowetty.investment.models.Stock

interface StockClickListener {
    fun onStockClick(stock: Stock)
}