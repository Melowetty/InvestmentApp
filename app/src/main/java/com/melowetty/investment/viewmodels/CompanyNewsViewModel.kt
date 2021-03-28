package com.melowetty.investment.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.melowetty.investment.models.CompanyNewsModel
import com.melowetty.investment.network.RetrofitFinancialModeling
import com.melowetty.investment.network.RetrofitFinhub
import com.melowetty.investment.network.RetrofitService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class CompanyNewsViewModel: ViewModel() {
    var newsList: MutableLiveData<List<CompanyNewsModel>> = MutableLiveData()

    fun getNewsListObserver(): MutableLiveData<List<CompanyNewsModel>> {
        return newsList
    }

    fun makeApiCall(ticker: String) {
        val retrofitInstance = RetrofitFinancialModeling.getRetrofitInstance().create(RetrofitService::class.java)
        retrofitInstance.getCompanyNews(mapOf("tickers" to ticker, "limit" to "15", "apikey" to "bb2a76dacd02fc18fadde7dc58bff592"))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(getNewsListObserverRx())
    }

    private fun getNewsListObserverRx(): Observer<List<CompanyNewsModel>> {
        return object : Observer<List<CompanyNewsModel>> {
            override fun onComplete() {
                // Hide progress bar
            }

            override fun onError(e: Throwable?) {
                newsList.postValue(null)
            }

            override fun onNext(t: List<CompanyNewsModel>?) {
                newsList.postValue(t)
            }

            override fun onSubscribe(d: Disposable?) {
                // Show progress bar
            }
        }
    }
}