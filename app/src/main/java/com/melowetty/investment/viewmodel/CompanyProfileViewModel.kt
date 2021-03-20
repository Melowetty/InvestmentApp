package com.melowetty.investment.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.melowetty.investment.models.CompanyProfileModel
import com.melowetty.investment.network.RetrofitFinhubInstance
import com.melowetty.investment.network.RetrofitService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class CompanyProfileViewModel: ViewModel() {
    var profile: MutableLiveData<CompanyProfileModel> = MutableLiveData()

    fun getCompanyProfileObserver(): MutableLiveData<CompanyProfileModel> {
        return profile
    }

    fun makeApiCall(symbol: String) {
        val retrofitInstance = RetrofitFinhubInstance.getRetrofitInstance().create(RetrofitService::class.java)
        retrofitInstance.getCompanyInfoFromAPI(symbol)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(getCompanyProfileObserverRx())
    }

    private fun getCompanyProfileObserverRx(): Observer<CompanyProfileModel> {
        return object : Observer<CompanyProfileModel> {
            override fun onComplete() {
                // Hide progress bar
            }

            override fun onError(e: Throwable?) {
                profile.postValue(null)
            }

            override fun onNext(t: CompanyProfileModel?) {
                profile.postValue(t)
            }

            override fun onSubscribe(d: Disposable?) {
                // Show progress bar
            }
        }
    }
}