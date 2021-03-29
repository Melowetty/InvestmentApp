package com.melowetty.investment.listeners

import com.melowetty.investment.models.NewsModel

interface NewsClickListener {
    fun onNewsClick(news: NewsModel)
}