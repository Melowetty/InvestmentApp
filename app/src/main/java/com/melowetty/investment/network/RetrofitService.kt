package com.melowetty.investment.network

import com.melowetty.investment.models.*
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface RetrofitService {
    @GET("index/constituents")
    fun getIndexConstituens(@Query("symbol") symbol: String): Observable<IndicesConstituensModel>
    @GET("forex/rates")
    fun getExchangeRate(@Query("base") base: String): Observable<ExchangeRateModel>
    @GET("company-news")
    fun getCompanyNews(@QueryMap filters: Map<String, String>): Observable<List<CompanyNewsModel>>
    @GET("profile/{tickers}")
    fun getCompaniesProfile(@Path("tickers") tickers: String, @Query("apikey") apikey: String): Observable<List<CompanyProfileModel>>
    @GET("tickers")
    fun searchStock(@QueryMap filters: Map<String, String>): Observable<FindStockModel>
}