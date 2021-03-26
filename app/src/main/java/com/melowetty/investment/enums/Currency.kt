package com.melowetty.investment.enums

import com.melowetty.investment.utils.Helper
import java.util.*

enum class Currency(val symbol: String) {
    USD("$") {
        override fun format(cost: Double): String {
            return if(cost >= 1000) {
                USD.symbol + Helper.spaceEveryThreeNums(cost.toInt())
            } else {
                USD.symbol + Helper.spaceEveryThreeNums(Helper.formatCost(cost),'.')
            }
        }
    },
    EUR("€") {
        override fun format(cost: Double): String {
            return if(cost >= 1000) {
                Helper.spaceEveryThreeNums(cost.toInt()) + " " + EUR.symbol
            } else {
                Helper.spaceEveryThreeNums(Helper.formatCost(cost),'.') + " " + EUR.symbol
            }
        }
    },
    RUB("₽") {
        override fun format(cost: Double): String {
            return if(cost >= 10000) {
                Helper.spaceEveryThreeNums(cost.toInt()) + " " + RUB.symbol
            } else {
                Helper.spaceEveryThreeNums(Helper.formatCost(cost),',') + " " + RUB.symbol
            }
        }
    };
    abstract fun format(cost: Double): String;
    companion object {
        fun getCardTypeByName(name: String): Currency {
            return try {
                valueOf(name.toUpperCase(Locale.ROOT))
            } catch (e: Exception) {
                USD
            }
        }
    }
}