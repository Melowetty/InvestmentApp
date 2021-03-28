package com.melowetty.investment.network

import com.melowetty.investment.models.*
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface RetrofitService {
    @GET("index/constituents")
    fun getIndexConstituents(@Query("symbol") symbol: String): Observable<IndicesConstituentsModel>
    @GET("forex/rates")
    fun getExchangeRate(@Query("base") base: String): Observable<ExchangeRateModel>
    @GET("stock_news")
    fun getCompanyNews(@QueryMap filters: Map<String, String>): Observable<List<NewsModel>>
    @GET("profile/{tickers}")
    fun getCompaniesProfile(@Path("tickers") tickers: String, @Query("apikey") apikey: String): Observable<List<ProfileModel>>
    @GET("tickers")
    fun searchStock(@QueryMap filters: Map<String, String>): Observable<FindStockModel>
    @GET("stock/candle")
    fun getCandles(@QueryMap filters: Map<String, String>): Observable<CandlesModel>
}