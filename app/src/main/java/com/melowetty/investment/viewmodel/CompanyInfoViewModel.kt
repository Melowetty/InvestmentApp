package com.melowetty.investment.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.melowetty.investment.models.CompanyInfoModel
import com.melowetty.investment.network.RetrofitService
import com.melowetty.investment.network.RetrofitYahooFinanceInstance
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class CompanyInfoViewModel: ViewModel() {
    var price: MutableLiveData<CompanyInfoModel> = MutableLiveData()

    fun getCompanyInfoObserver(): MutableLiveData<CompanyInfoModel> {
        return price
    }

    fun makeApiCall(ticker: String) {
        val retrofitInstance = RetrofitYahooFinanceInstance.getRetrofitInstance().create(RetrofitService::class.java)
        retrofitInstance.getCompanyInfo(ticker)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(getCompanyInfoObserverRx())
    }

    private fun getCompanyInfoObserverRx(): Observer<CompanyInfoModel> {
        return object : Observer<CompanyInfoModel> {
            override fun onComplete() {
                // Hide progress bar
            }

            override fun onError(e: Throwable?) {
                price.postValue(null)
            }

            override fun onNext(t: CompanyInfoModel?) {
                price.postValue(t)
            }

            override fun onSubscribe(d: Disposable?) {
                // Show progress bar
            }
        }
    }

}