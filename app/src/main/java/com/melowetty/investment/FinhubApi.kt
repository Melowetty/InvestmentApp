package com.melowetty.investment

import com.finnhub.api.apis.DefaultApi
import com.finnhub.api.infrastructure.ApiClient

class FinhubApi {
    lateinit var apiClient: DefaultApi
    fun init(): DefaultApi {
        ApiClient.apiKey["token"] = "c14aug748v6t8t43aqqg"
        apiClient = DefaultApi()
        return apiClient;
    }
    fun getLatestCost(symbol: String): Float? {
        return apiClient.quote(symbol).c
    }
}