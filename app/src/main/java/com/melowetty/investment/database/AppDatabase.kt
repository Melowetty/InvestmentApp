package com.melowetty.investment.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.melowetty.investment.database.models.FavoriteStock
import com.melowetty.investment.database.models.FoundTicker

@Database(entities = [FavoriteStock::class, FoundTicker::class], version = 4)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteStockDao(): FavoritesDao
    abstract fun foundTickersDao(): FoundItemsDao
}