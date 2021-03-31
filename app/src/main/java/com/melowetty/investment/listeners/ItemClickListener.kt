package com.melowetty.investment.listeners

import com.melowetty.investment.database.models.FoundTicker

interface ItemClickListener {
    fun onItemClick(ticker: FoundTicker)
}