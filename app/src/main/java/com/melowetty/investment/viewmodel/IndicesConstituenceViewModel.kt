package com.melowetty.investment.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.melowetty.investment.models.IndicesConstituensModel
import com.melowetty.investment.network.RetrofitFinhub
import com.melowetty.investment.network.RetrofitService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class IndicesConstituenceViewModel: ViewModel() {
    var constituenceList: MutableLiveData<IndicesConstituensModel> = MutableLiveData()

    fun getConstituenceObserver(): MutableLiveData<IndicesConstituensModel> {
        return constituenceList
    }

    fun makeApiCall(ticker: String) {
        val retrofitInstance = RetrofitFinhub.getRetrofitInstance().create(
            RetrofitService::class.java)
        retrofitInstance.getIndexConstituens(ticker)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(getPriceObserverRx())
    }

    private fun getPriceObserverRx(): Observer<IndicesConstituensModel> {
        return object : Observer<IndicesConstituensModel> {
            override fun onComplete() {
                // Hide progress bar
            }

            override fun onError(e: Throwable?) {
                constituenceList.postValue(null)
            }

            override fun onNext(t: IndicesConstituensModel?) {
                constituenceList.postValue(t)
            }

            override fun onSubscribe(d: Disposable?) {
                // Show progress bar
            }
        }
    }
}