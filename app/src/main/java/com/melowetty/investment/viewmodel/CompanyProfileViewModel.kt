package com.melowetty.investment.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.melowetty.investment.models.CompanyProfileModel
import com.melowetty.investment.network.RetrofitFinancialModelingInstance
import com.melowetty.investment.network.RetrofitService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class CompanyProfileViewModel: ViewModel() {
    var price: MutableLiveData<List<CompanyProfileModel>> = MutableLiveData()

    fun getCompanyProfileObserver(): MutableLiveData<List<CompanyProfileModel>> {
        return price
    }

    fun makeApiCall(ticker: String) {
        val retrofitInstance = RetrofitFinancialModelingInstance.getRetrofitInstance().create(
            RetrofitService::class.java)
        retrofitInstance.getCompaniesProfile(tickers = ticker, apikey = "bb2a76dacd02fc18fadde7dc58bff592")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(getCompanyProfileObserverRx())
    }

    private fun getCompanyProfileObserverRx(): Observer<List<CompanyProfileModel>> {
        return object : Observer<List<CompanyProfileModel>> {
            override fun onComplete() {
                // Hide progress bar
            }

            override fun onError(e: Throwable?) {
                price.postValue(null)
            }

            override fun onNext(t: List<CompanyProfileModel>?) {
                price.postValue(t)
            }

            override fun onSubscribe(d: Disposable?) {
                // Show progress bar
            }
        }
    }
}