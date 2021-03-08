package com.melowetty.investment

import android.graphics.drawable.Drawable

class StockItem(
    val title: String,
    val subtitle: String,
    val logo: Drawable?,
    val cost: Double,
    val difference: Double,
    private val growth: Boolean
)