package com.melowetty.investment.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.melowetty.investment.models.ExchangeRateModel
import com.melowetty.investment.network.RetrofitFinhub
import com.melowetty.investment.network.RetrofitService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class ExchangeRateViewModel: ViewModel() {
    var exchangeRate: MutableLiveData<ExchangeRateModel> = MutableLiveData()

    fun getExchangeRateObserver(): MutableLiveData<ExchangeRateModel> {
        return exchangeRate
    }

    fun makeApiCall(base: String) {
        val retrofitInstance = RetrofitFinhub.getRetrofitInstance().create(
            RetrofitService::class.java)
        retrofitInstance.getExchangeRate(base)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(getExchangeRateObserverRx())
    }

    private fun getExchangeRateObserverRx(): Observer<ExchangeRateModel> {
        return object : Observer<ExchangeRateModel> {
            override fun onComplete() {
                // Hide progress bar
            }

            override fun onError(e: Throwable?) {
                exchangeRate.postValue(null)
            }

            override fun onNext(t: ExchangeRateModel?) {
                exchangeRate.postValue(t)
            }

            override fun onSubscribe(d: Disposable?) {
                // Show progress bar
            }
        }
    }
}