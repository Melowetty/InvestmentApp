package com.melowetty.investment.database

import androidx.room.*
import com.melowetty.investment.database.models.SearchedItem


@Dao
interface HistorySearchDao {
    @Query("SELECT * FROM searcheditem")
    fun getAll(): List<SearchedItem>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(ticker: SearchedItem)

    @Delete
    fun delete(ticker: SearchedItem)
}