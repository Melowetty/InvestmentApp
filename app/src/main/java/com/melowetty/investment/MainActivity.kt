package com.melowetty.investment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.melowetty.investment.models.CompanyProfileModel
import com.melowetty.investment.models.RealTimePriceModel
import com.melowetty.investment.models.StockListModel
import com.melowetty.investment.utils.Helper
import com.melowetty.investment.viewmodel.CompanyProfileViewModel
import com.melowetty.investment.viewmodel.PriceViewModel
import com.melowetty.investment.viewmodel.SearchActivityViewModel

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
        getRealTimePrice("AAPL")
        getRealTimePrice("YNDX")
    }
    fun initRecyclerView() {
        recyclerView.apply {
            val layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }
    fun getRealTimePrice(input: String) {
        val viewModel = ViewModelProvider(this).get(PriceViewModel::class.java)
        viewModel.getPriceObserver().observe(this, Observer<RealTimePriceModel> { it ->
            if(it != null) {
                Log.d(TAG, it.toString())
            }
            else {
                Log.e(TAG, "Error in fetching data")
            }
        })
        viewModel.makeApiCall(input)
    }
}
