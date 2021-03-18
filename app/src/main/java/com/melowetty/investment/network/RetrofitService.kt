package com.melowetty.investment.network

import com.melowetty.investment.models.StockListModel
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitService {
    @GET("search")
    fun getQueryListFromApi(@Query("q") query: String): Observable<StockListModel>
}