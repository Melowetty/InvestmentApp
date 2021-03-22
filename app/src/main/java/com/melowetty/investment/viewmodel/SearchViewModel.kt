package com.melowetty.investment.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.melowetty.investment.models.SearchModel
import com.melowetty.investment.network.RetrofitService
import com.melowetty.investment.network.RetrofitTwelveDataInstance
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class SearchViewModel: ViewModel() {
    var findetStocks: MutableLiveData<SearchModel> = MutableLiveData()

    fun getFindetStocksObserver(): MutableLiveData<SearchModel> {
        return findetStocks
    }

    fun makeApiCall(query: String) {
        val retrofitInstance = RetrofitTwelveDataInstance.getRetrofitInstance().create(RetrofitService::class.java)
        retrofitInstance.searchStocks(mapOf("symbol" to query, "source" to "docs", "apikey" to "7d2e176f6391445ea1bb7930e7bb3be2"))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(getFindetStocksObserverRx())
    }

    private fun getFindetStocksObserverRx(): Observer<SearchModel> {
        return object : Observer<SearchModel> {
            override fun onComplete() {
                // Hide progress bar
            }

            override fun onError(e: Throwable?) {
                findetStocks.postValue(null)
            }

            override fun onNext(t: SearchModel?) {
                findetStocks.postValue(t)
            }

            override fun onSubscribe(d: Disposable?) {
                // Show progress bar
            }
        }
    }
}