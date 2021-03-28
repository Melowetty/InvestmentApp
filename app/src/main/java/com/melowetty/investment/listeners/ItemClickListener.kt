package com.melowetty.investment.listeners

import com.melowetty.investment.database.models.SearchedItem

interface ItemClickListener {
    fun onItemClick(ticker: SearchedItem)
}