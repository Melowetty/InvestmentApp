package com.melowetty.investment

import android.util.Log
import com.finnhub.api.apis.DefaultApi
import com.finnhub.api.infrastructure.ApiClient
import com.finnhub.api.models.CompanyProfile2

class CompanyProfile(val ticker: String) {
    private val TAG = this::class.java.simpleName
    var logo: String? = null
    var company: String? = null
    var currency: Currency? = null
    var profile: CompanyProfile2? = null

    init {
        ApiClient.apiKey["token"] = "c14aug748v6t8t43aqqg"
        val api = DefaultApi()
        profile = api.companyProfile2(ticker, null, null)
        Log.d(TAG, "Profile successfull completed")
        logo = profile!!.logo
        company = profile!!.name
        currency = Currency.getCardTypeByName(profile!!.currency!!)
        Log.d(TAG, "$logo, $company, ${currency.toString()}")
    }
    fun getStockItem(): StockItem = StockItem(ticker, company, logo, 256.0, 2.01, true)
}