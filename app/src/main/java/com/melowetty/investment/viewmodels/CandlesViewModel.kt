package com.melowetty.investment.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.melowetty.investment.enums.Interval
import com.melowetty.investment.models.CandlesModel
import com.melowetty.investment.network.RetrofitFinnhub
import com.melowetty.investment.network.RetrofitService
import com.melowetty.investment.utils.Helper
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class CandlesViewModel: ViewModel() {
    var candles: MutableLiveData<CandlesModel> = MutableLiveData()

    fun getCandlesObserver(): MutableLiveData<CandlesModel> {
        return candles
    }

    fun getCandles(ticker: String, interval: Interval) {
        val to = Helper.getUnixTime().toString()
        val retrofitInstance = RetrofitFinnhub.getRetrofitInstance().create(
            RetrofitService::class.java)
        retrofitInstance.getCandles(
            mapOf("symbol" to ticker,
                "resolution" to interval.resolution.string,
                "from" to interval.from.toString(),
                "to" to to))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(getCandlesObserverRx())
    }

    private fun getCandlesObserverRx(): Observer<CandlesModel> {
        return object : Observer<CandlesModel> {
            override fun onComplete() {
                // Hide progress bar
            }

            override fun onError(e: Throwable?) {
                candles.postValue(null)
            }

            override fun onNext(t: CandlesModel?) {
                candles.postValue(t)
            }

            override fun onSubscribe(d: Disposable?) {
                // Show progress bar
            }
        }
    }
}