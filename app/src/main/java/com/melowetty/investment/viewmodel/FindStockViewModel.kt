package com.melowetty.investment.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.melowetty.investment.models.FindStockModel
import com.melowetty.investment.network.RetrofitMarketStack
import com.melowetty.investment.network.RetrofitService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class FindStockViewModel: ViewModel() {
    var findStocks: MutableLiveData<FindStockModel> = MutableLiveData()

    fun getFindStocksObserver(): MutableLiveData<FindStockModel> {
        return findStocks
    }

    fun makeApiCall(query: String) {
        val retrofitInstance = RetrofitMarketStack.getRetrofitInstance().create(RetrofitService::class.java)
        retrofitInstance.searchStock(mapOf("search" to query, "access_key" to "65399ca190176f7d6152f5674cff3f87"))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(getFindStocksObserverRx())
    }

    private fun getFindStocksObserverRx(): Observer<FindStockModel> {
        return object : Observer<FindStockModel> {
            override fun onComplete() {
                // Hide progress bar
            }

            override fun onError(e: Throwable?) {
                findStocks.postValue(null)
            }

            override fun onNext(t: FindStockModel?) {
                findStocks.postValue(t)
            }

            override fun onSubscribe(d: Disposable?) {
                // Show progress bar
            }
        }
    }
}