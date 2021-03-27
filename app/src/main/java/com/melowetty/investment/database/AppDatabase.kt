package com.melowetty.investment.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.melowetty.investment.database.models.FavouriteStock

@Database(entities = [FavouriteStock::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favouriteStockDao(): FavouritesDao
}