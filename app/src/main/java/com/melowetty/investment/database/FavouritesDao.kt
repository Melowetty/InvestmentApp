package com.melowetty.investment.database

import androidx.room.*
import com.melowetty.investment.database.models.FavouriteStock

@Dao
interface FavouritesDao {
    @Query("SELECT * FROM favouriteStock")
    fun getAll(): List<FavouriteStock>

    @Query("SELECT * FROM favouriteStock WHERE ticker = :ticker")
    fun getBy(ticker: String): FavouriteStock

    @Insert
    fun insert(favouriteStock: FavouriteStock)

    @Delete
    fun delete(favouriteStock: FavouriteStock)

    @Update
    fun update(favouriteStock: FavouriteStock)
}