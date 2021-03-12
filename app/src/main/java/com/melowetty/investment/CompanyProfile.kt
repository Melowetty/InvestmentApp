package com.melowetty.investment

import com.finnhub.api.models.CompanyProfile2

class CompanyProfile(val ticker: String) {
    var logo: String? = null
    var company: String? = null
    var currency: Currency? = null
    var profile: CompanyProfile2? = null

    fun init() {
        profile = FinhubApi().init().companyProfile2(ticker, null, null)
        logo = profile!!.logo
        currency = Currency.valueOf(profile!!.currency!!)
        company = profile!!.name
    }
}