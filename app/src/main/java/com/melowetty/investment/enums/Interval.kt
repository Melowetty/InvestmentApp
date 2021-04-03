package com.melowetty.investment.enums

import com.melowetty.investment.utils.Helper

enum class Interval(
    val from: Long,
    val resolution: Resolution) {
    DAY(Helper.getUnixTime() - Helper.DAY * 2, Resolution.PER_15MIN),
    WEEK(Helper.getUnixTime() - Helper.WEEK, Resolution.PER_30MIN),
    MONTH(Helper.getUnixTime() - Helper.MONTH, Resolution.PER_HOUR),
    SIX_MONTH(Helper.getUnixTime() - Helper.MONTH * 6, Resolution.PER_DAY),
    YEAR(Helper.getUnixTime() - Helper.YEAR, Resolution.PER_DAY),
    FIVE_YEAR(Helper.getUnixTime() - Helper.YEAR * 5, Resolution.PER_WEEK)
}