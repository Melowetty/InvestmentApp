package com.melowetty.investment.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.melowetty.investment.models.StockListModel
import com.melowetty.investment.network.RetrofitFinhubInstance
import com.melowetty.investment.network.RetrofitService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class SearchActivityViewModel: ViewModel() {
    var stockList: MutableLiveData<StockListModel> = MutableLiveData()

    fun getStockListObserver(): MutableLiveData<StockListModel> {
        return stockList
    }

    fun makeApiCall(query: String) {
        val retrofitInstance = RetrofitFinhubInstance.getRetrofitInstance().create(RetrofitService::class.java)
        retrofitInstance.getQueryListFromApi(query)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(getStockListObserverRx())
    }

    private fun getStockListObserverRx(): Observer<StockListModel> {
        return object : Observer<StockListModel> {
            override fun onComplete() {
                // Hide progress bar
            }

            override fun onError(e: Throwable?) {
                stockList.postValue(null)
            }

            override fun onNext(t: StockListModel?) {
                stockList.postValue(t)
            }

            override fun onSubscribe(d: Disposable?) {
                // Show progress bar
            }
        }
    }
}