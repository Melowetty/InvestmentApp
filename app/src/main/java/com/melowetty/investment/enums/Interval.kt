package com.melowetty.investment.enums

import com.melowetty.investment.utils.Helper

enum class Interval(val from: Long,
                    val resolution: Resolution) {
    DAY(Helper.getUnixTime() - Helper.day * 3, Resolution.PER_15MIN),
    WEEK(Helper.getUnixTime() - Helper.week, Resolution.PER_30MIN),
    MONTH(Helper.getUnixTime() - Helper.month, Resolution.PER_HOUR),
    SIX_MONTH(Helper.getUnixTime() - Helper.month * 6, Resolution.PER_DAY),
    YEAR(Helper.getUnixTime() - Helper.year, Resolution.PER_DAY),
    FIVE_YEAR(Helper.getUnixTime() - Helper.year * 5, Resolution.PER_WEEK)
}