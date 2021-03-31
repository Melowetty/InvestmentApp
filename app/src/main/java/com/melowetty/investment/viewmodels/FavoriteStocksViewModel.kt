package com.melowetty.investment.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.melowetty.investment.AppActivity
import com.melowetty.investment.database.models.FavoriteStock

class FavoriteStocksViewModel: ViewModel() {
    var favoriteStocks: MutableLiveData<List<FavoriteStock>> = MutableLiveData()

    init {
        favoriteStocks.value = AppActivity.getDatabase()?.favoriteStockDao()?.getAll()
    }

    fun updateFavouriteStocks() {
        favoriteStocks.value = AppActivity.getDatabase()?.favoriteStockDao()?.getAll()
    }

}