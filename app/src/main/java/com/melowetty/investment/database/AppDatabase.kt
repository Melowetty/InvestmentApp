package com.melowetty.investment.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.melowetty.investment.database.models.FavouriteStock
import com.melowetty.investment.database.models.SearchedItem

@Database(entities = [FavouriteStock::class, SearchedItem::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favouriteStockDao(): FavouritesDao
    abstract fun historySearchDao(): HistorySearchDao
}