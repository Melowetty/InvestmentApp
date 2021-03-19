package com.melowetty.investment.network

import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitFinhubInstance {

    companion object {
        val baseURL = "https://finnhub.io/api/v1/"

        fun getRetrofitInstance() : Retrofit {
            val client: OkHttpClient = OkHttpClient.Builder().addInterceptor { chain ->
                val newRequest: Request = chain.request().newBuilder()
                    .addHeader("X-Finnhub-Token", "c14aug748v6t8t43aqqg")
                    .build()
                chain.proceed(newRequest)
            }.build()

            return Retrofit.Builder()
                .baseUrl(baseURL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build()
        }
    }
}