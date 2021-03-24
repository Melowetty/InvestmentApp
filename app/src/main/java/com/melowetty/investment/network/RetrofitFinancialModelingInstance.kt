package com.melowetty.investment.network

import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitFinancialModelingInstance {
    companion object {
        private const val baseURL = "https://financialmodelingprep.com/api/v3/"

        fun getRetrofitInstance() : Retrofit {

            return Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build()
        }
    }
}