package com.melowetty.investment.network

import com.melowetty.investment.models.CompanyProfileModel
import com.melowetty.investment.models.RealTimePriceModel
import com.melowetty.investment.models.StockListModel
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitService {
    @GET("search")
    fun getQueryListFromApi(@Query("q") query: String): Observable<StockListModel>
    @GET("stock/profile2")
    fun getCompanyInfoFromAPI(@Query("symbol") symbol: String): Observable<CompanyProfileModel>
    @GET("v10/finance/quoteSummary/{ticker}?modules=price")
    fun getRealTimePrice(@Path("ticker") ticker: String): Observable<RealTimePriceModel>
}