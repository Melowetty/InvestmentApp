package com.melowetty.investment.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.melowetty.investment.models.CompanyNewsModel
import com.melowetty.investment.network.RetrofitFinhubInstance
import com.melowetty.investment.network.RetrofitService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class CompanyNewsViewModel: ViewModel() {
    var newsList: MutableLiveData<CompanyNewsModel> = MutableLiveData()

    fun getNewsListObserver(): MutableLiveData<CompanyNewsModel> {
        return newsList
    }

    fun makeApiCall(ticker: String, from: String, to: String) {
        val retrofitInstance = RetrofitFinhubInstance.getRetrofitInstance().create(RetrofitService::class.java)
        retrofitInstance.getCompanyNews(ticker, from, to)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(getNewsListObserverRx())
    }

    private fun getNewsListObserverRx(): Observer<CompanyNewsModel> {
        return object : Observer<CompanyNewsModel> {
            override fun onComplete() {
                // Hide progress bar
            }

            override fun onError(e: Throwable?) {
                newsList.postValue(null)
            }

            override fun onNext(t: CompanyNewsModel?) {
                newsList.postValue(t)
            }

            override fun onSubscribe(d: Disposable?) {
                // Show progress bar
            }
        }
    }
}