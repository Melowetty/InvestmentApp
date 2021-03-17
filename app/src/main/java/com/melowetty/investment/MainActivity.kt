package com.melowetty.investment

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.melowetty.investment.utils.Helper

class MainActivity : AppCompatActivity() {
    private val TAG = this::class.java.simpleName
    private lateinit var favourite: TextView
    private lateinit var stocks: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchBar: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        favourite = findViewById(R.id.favourite)
        stocks = findViewById(R.id.stocks)
        searchBar = findViewById(R.id.search_bar)

        favourite.setOnClickListener {
            Helper.changeCondition(favourite, true)
            Helper.changeCondition(stocks, false)
        }
        stocks.setOnClickListener {
            Helper.changeCondition(favourite, false)
            Helper.changeCondition(stocks, true)
            val stockView = Intent(this, StockView::class.java)
            startActivity(stockView)
        }
        searchBar.setOnClickListener {
            val stockView = Intent(this, SearchActivity::class.java)
            startActivity(stockView)
        }
    }
}
