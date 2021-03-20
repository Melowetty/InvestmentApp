package com.melowetty.investment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.melowetty.investment.models.RealTimePriceModel
import com.melowetty.investment.utils.Helper
import com.melowetty.investment.viewmodel.PriceViewModel

class MainActivity : AppCompatActivity() {
    private val TAG = this::class.java.simpleName
    private lateinit var favourite: TextView
    private lateinit var stocks: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchBar: TextView
    private lateinit var priceModel: PriceViewModel
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

        initPriceModel()

        getRealTimePrice("AAPL")
        getRealTimePrice("YNDX")
        getRealTimePrice("TSLA")
    }
    fun initPriceModel() {
        priceModel = ViewModelProvider(this).get(PriceViewModel::class.java)
        priceModel.getPriceObserver().observe(this, Observer<RealTimePriceModel> { it ->
            if(it != null) {
                Log.d(TAG, it.toString())
            }
            else {
                Log.e(TAG, "Error in fetching data")
            }
        })
    }
    fun initRecyclerView() {
        recyclerView.apply {
            val layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }
    fun getRealTimePrice(input: String) {
        priceModel.makeApiCall(input)
    }
}
