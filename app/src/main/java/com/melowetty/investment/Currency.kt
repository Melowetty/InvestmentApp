package com.melowetty.investment

enum class Currency(val currency: String) {
    USD("$") {
        override fun format(cost: Float): String {
            TODO("Not yet implemented")
        }
    },
    EUR("€") {
        override fun format(cost: Float): String {
            TODO("Not yet implemented")
        }
    },
    RUB("₽") {
        override fun format(cost: Float): String {
            TODO("Not yet implemented")
        }
    };
    abstract fun format(cost: Float): String;
    companion object {
        fun getCardTypeByName(name: String) = valueOf(name.toUpperCase())
    }
}