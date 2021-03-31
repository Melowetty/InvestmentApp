package com.melowetty.investment.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.melowetty.investment.AppActivity
import com.melowetty.investment.database.models.FoundTicker

class SearchHistoryViewModel: ViewModel() {
    var historySearch: MutableLiveData<List<FoundTicker>> = MutableLiveData()

    init {
        historySearch.value = AppActivity.getDatabase()?.foundTickersDao()?.getAll()
    }

    fun updateSearchedStocks() {
        historySearch.value = AppActivity.getDatabase()?.foundTickersDao()?.getAll()
    }
}