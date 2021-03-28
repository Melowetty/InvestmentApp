package com.melowetty.investment.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.melowetty.investment.AppActivity
import com.melowetty.investment.database.models.SearchedItem

class SearchHistoryViewModel: ViewModel() {
    var historySearch: MutableLiveData<List<SearchedItem>> = MutableLiveData()

    init {
        historySearch.value = AppActivity.getDatabase()?.historySearchDao()?.getAll()
    }

    fun updateSearchedStocks() {
        historySearch.value = AppActivity.getDatabase()?.historySearchDao()?.getAll()
    }
}