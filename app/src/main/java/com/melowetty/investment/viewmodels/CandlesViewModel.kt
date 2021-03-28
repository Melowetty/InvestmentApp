package com.melowetty.investment.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.melowetty.investment.enums.Resolution
import com.melowetty.investment.models.CandlesModel
import com.melowetty.investment.network.RetrofitFinhub
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

    fun getCandlesFromYear(ticker: String) {
        val from = Helper.getUnixTime() - Helper.year
        val to = Helper.getUnixTime()
        val retrofitInstance = RetrofitFinhub.getRetrofitInstance().create(
            RetrofitService::class.java)
        retrofitInstance.getCandles(mapOf("symbol" to ticker, "resolution" to Resolution.PER_DAY.resolution, "from" to from.toString(), "to" to to.toString()))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(getCandlesObserverRx())
    }

    fun getCandlesFrom5Year(ticker: String) {
        val from = Helper.getUnixTime() - (Helper.year) * 5
        val to = Helper.getUnixTime()
        val retrofitInstance = RetrofitFinhub.getRetrofitInstance().create(
            RetrofitService::class.java)
        retrofitInstance.getCandles(mapOf("symbol" to ticker, "resolution" to Resolution.PER_DAY.resolution, "from" to from.toString(), "to" to to.toString()))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(getCandlesObserverRx())
    }

    fun getCandlesFromDay(ticker: String) {
        val from = Helper.getUnixTime() - Helper.day*3
        val to = Helper.getUnixTime()
        val retrofitInstance = RetrofitFinhub.getRetrofitInstance().create(
            RetrofitService::class.java)
        retrofitInstance.getCandles(mapOf("symbol" to ticker, "resolution" to Resolution.PER_HOUR.resolution, "from" to from.toString(), "to" to to.toString()))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(getCandlesObserverRx())
    }

    fun getCandlesFromWeek(ticker: String) {
        val from = Helper.getUnixTime() - Helper.week
        val to = Helper.getUnixTime()
        val retrofitInstance = RetrofitFinhub.getRetrofitInstance().create(
            RetrofitService::class.java)
        retrofitInstance.getCandles(mapOf("symbol" to ticker, "resolution" to Resolution.PER_HOUR.resolution, "from" to from.toString(), "to" to to.toString()))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(getCandlesObserverRx())
    }

    fun getCandlesFromMonth(ticker: String) {
        val from = Helper.getUnixTime() - Helper.month
        val to = Helper.getUnixTime()
        val retrofitInstance = RetrofitFinhub.getRetrofitInstance().create(
            RetrofitService::class.java)
        retrofitInstance.getCandles(mapOf("symbol" to ticker, "resolution" to Resolution.PER_DAY.resolution, "from" to from.toString(), "to" to to.toString()))
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