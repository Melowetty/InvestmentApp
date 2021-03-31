package com.melowetty.investment.enums

enum class Resolution(val string: String) {
    PER_15MIN("15"),
    PER_30MIN("30"),
    PER_HOUR("60"),
    PER_DAY("D"),
    PER_WEEK("W"),
    PER_MONTH("M")
}