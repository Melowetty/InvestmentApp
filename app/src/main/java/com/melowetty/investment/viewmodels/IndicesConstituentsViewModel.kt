package com.melowetty.investment.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.melowetty.investment.models.IndicesConstituentsModel
import com.melowetty.investment.network.RetrofitFinnhub
import com.melowetty.investment.network.RetrofitService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class IndicesConstituentsViewModel: ViewModel() {
    var constituentsList: MutableLiveData<IndicesConstituentsModel> = MutableLiveData()

    fun getConstituentsObserver(): MutableLiveData<IndicesConstituentsModel> {
        return constituentsList
    }

    fun makeApiCall(ticker: String) {
        val retrofitInstance = RetrofitFinnhub.getRetrofitInstance().create(
            RetrofitService::class.java)
        retrofitInstance.getIndexConstituents(ticker)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(getPriceObserverRx())
    }

    private fun getPriceObserverRx(): Observer<IndicesConstituentsModel> {
        return object : Observer<IndicesConstituentsModel> {
            override fun onComplete() {
                // Hide progress bar
            }

            override fun onError(e: Throwable?) {
                constituentsList.postValue(null)
            }

            override fun onNext(t: IndicesConstituentsModel?) {
                constituentsList.postValue(t)
            }

            override fun onSubscribe(d: Disposable?) {
                // Show progress bar
            }
        }
    }
}