package com.melowetty.investment.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.melowetty.investment.models.ProfileModel
import com.melowetty.investment.network.RetrofitFinancialModeling
import com.melowetty.investment.network.RetrofitService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class ProfileViewModel: ViewModel() {
    var price: MutableLiveData<List<ProfileModel>> = MutableLiveData()

    fun getCompanyProfileObserver(): MutableLiveData<List<ProfileModel>> {
        return price
    }

    fun makeApiCall(ticker: String) {
        val retrofitInstance = RetrofitFinancialModeling.getRetrofitInstance().create(
            RetrofitService::class.java)
        retrofitInstance.getCompaniesProfile(tickers = ticker, apikey = "bb2a76dacd02fc18fadde7dc58bff592")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(getCompanyProfileObserverRx())
    }

    private fun getCompanyProfileObserverRx(): Observer<List<ProfileModel>> {
        return object : Observer<List<ProfileModel>> {
            override fun onComplete() {
                // Hide progress bar
            }

            override fun onError(e: Throwable?) {
                price.postValue(null)
            }

            override fun onNext(t: List<ProfileModel>?) {
                price.postValue(t)
            }

            override fun onSubscribe(d: Disposable?) {
                // Show progress bar
            }
        }
    }
}