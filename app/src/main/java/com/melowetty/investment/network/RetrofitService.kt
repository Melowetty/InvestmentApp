package com.melowetty.investment.network

import com.melowetty.investment.models.*
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface RetrofitService {
    @GET("search")
    fun getQueryListFromApi(@Query("q") query: String): Observable<StockListModel>
    @GET("v10/finance/quoteSummary/{ticker}?modules=price")
    fun getCompanyInfo(@Path("ticker") ticker: String): Observable<CompanyInfoModel>
    @GET("index/constituents")
    fun getIndexConstituens(@Query("symbol") symbol: String): Observable<IndicesConstituensModel>
    @GET("forex/rates")
    fun getExchangeRate(@Query("base") base: String): Observable<ExchangeRateModel>
    @GET("company-news")
    fun getCompanyNews(@QueryMap filters: Map<String, String>): Observable<CompanyNewsModel>
    @GET("symbol_search")
    fun searchStocks(@QueryMap filters: Map<String, String>): Observable<SearchModel>
}