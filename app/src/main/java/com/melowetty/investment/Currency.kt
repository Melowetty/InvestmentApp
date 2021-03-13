package com.melowetty.investment

import com.melowetty.investment.utils.Helper
import java.util.*

enum class Currency(val currency: String) {
    USD("$") {
        override fun format(cost: Float): String {
            return if(cost >= 1000) {
                USD.currency + Helper.spaceEveryThreeNums(cost.toInt())
            } else {
                USD.currency + Helper.spaceEveryThreeNums(Helper.formatCost(cost),'.')
            }
        }
    },
    EUR("€") {
        override fun format(cost: Float): String {
            return if(cost >= 1000) {
                Helper.spaceEveryThreeNums(cost.toInt()) + " " + EUR.currency
            } else {
                Helper.spaceEveryThreeNums(Helper.formatCost(cost),'.') + " " + EUR.currency
            }
        }
    },
    RUB("₽") {
        override fun format(cost: Float): String {
            return if(cost >= 10000) {
                Helper.spaceEveryThreeNums(cost.toInt()) + " " + RUB.currency
            } else {
                Helper.spaceEveryThreeNums(Helper.formatCost(cost),',') + " " + RUB.currency
            }
        }
    };
    abstract fun format(cost: Float): String;
    companion object {
        fun getCardTypeByName(name: String) = valueOf(name.toUpperCase(Locale.ROOT))
    }
}