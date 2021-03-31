package com.melowetty.investment.database

import androidx.room.*
import com.melowetty.investment.database.models.FavoriteStock

@Dao
interface FavoritesDao {
    @Query("SELECT * FROM favorite_stocks")
    fun getAll(): List<FavoriteStock>

    @Query("SELECT * FROM favorite_stocks WHERE ticker = :ticker")
    fun getBy(ticker: String): FavoriteStock

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(favoriteStock: FavoriteStock)

    @Delete
    fun delete(favoriteStock: FavoriteStock)

    @Update
    fun update(favoriteStock: FavoriteStock)
}