package com.melowetty.investment.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.melowetty.investment.AppActivity
import com.melowetty.investment.database.models.FavouriteStock

class FavouriteStocksViewModel: ViewModel() {
    var favouriteStocks: MutableLiveData<List<FavouriteStock>> = MutableLiveData()

    init {
        favouriteStocks.value = AppActivity.getDatabase()?.favouriteStockDao()?.getAll()
    }

    fun updateFavouriteStocks() {
        favouriteStocks.value = AppActivity.getDatabase()?.favouriteStockDao()?.getAll()
    }

}