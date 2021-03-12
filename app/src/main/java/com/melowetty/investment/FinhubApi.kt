package com.melowetty.investment

import com.finnhub.api.apis.DefaultApi
import com.finnhub.api.infrastructure.ApiClient

class FinhubApi {
    lateinit var apiClient: DefaultApi
    fun init() {
        apiClient = DefaultApi()
        ApiClient.apiKey["token"] = "c14aug748v6t8t43aqqg"
    }
    fun getLatestCost(symbol: String): Float? {
        return apiClient.quote(symbol).c
    }
    fun getCompanyInfo(symbol: String) {
        apiClient.companyProfile2(symbol, null, null)
    }
}