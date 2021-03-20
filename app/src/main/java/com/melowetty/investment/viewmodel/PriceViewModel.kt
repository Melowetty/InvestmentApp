package com.melowetty.investment.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.melowetty.investment.models.RealTimePriceModel
import com.melowetty.investment.network.RetrofitService
import com.melowetty.investment.network.RetrofitYahooFinanceInstance
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class PriceViewModel: ViewModel() {
    var price: MutableLiveData<RealTimePriceModel> = MutableLiveData()

    fun getPriceObserver(): MutableLiveData<RealTimePriceModel> {
        return price
    }

    fun makeApiCall(ticker: String) {
        val retrofitInstance = RetrofitYahooFinanceInstance.getRetrofitInstance().create(RetrofitService::class.java)
        retrofitInstance.getRealTimePrice(ticker)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(getPriceObserverRx())
    }

    private fun getPriceObserverRx(): Observer<RealTimePriceModel> {
        return object : Observer<RealTimePriceModel> {
            override fun onComplete() {
                // Hide progress bar
            }

            override fun onError(e: Throwable?) {
                price.postValue(null)
            }

            override fun onNext(t: RealTimePriceModel?) {
                price.postValue(t)
            }

            override fun onSubscribe(d: Disposable?) {
                // Show progress bar
            }
        }
    }

}