package com.melowetty.investment.database

import androidx.room.*
import com.melowetty.investment.database.models.FoundTicker


@Dao
interface FoundItemsDao {
    @Query("SELECT * FROM search_history")
    fun getAll(): List<FoundTicker>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(ticker: FoundTicker)

    @Delete
    fun delete(ticker: FoundTicker)
}