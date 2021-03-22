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
import com.melowetty.investment.models.*
import com.melowetty.investment.utils.Helper
import com.melowetty.investment.viewmodel.CompanyInfoViewModel
import com.melowetty.investment.viewmodel.CompanyNewsViewModel
import com.melowetty.investment.viewmodel.ExchangeRateViewModel
import com.melowetty.investment.viewmodel.IndicesConstituenceViewModel

class MainActivity : AppCompatActivity() {
    private val TAG = this::class.java.simpleName

    private lateinit var favourite: TextView
    private lateinit var stocks: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchBar: TextView

    private var stockList: ArrayList<Stock> = ArrayList()

    private lateinit var adapter: StockAdapter

    private lateinit var companyInfoModel: CompanyInfoViewModel
    private lateinit var incideConstituensModel: IndicesConstituenceViewModel
    private lateinit var exchangeRateModel: ExchangeRateViewModel
    private lateinit var companyNewsModel: CompanyNewsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter = StockAdapter(arrayListOf())
        recyclerView.adapter = adapter
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
            Log.d(TAG, stockList.size.toString())
        }
        searchBar.setOnClickListener {
            val stockView = Intent(this, SearchActivity::class.java)
            startActivity(stockView)
        }

        initModels()

        getIndexConstituens(Indices.NASDAQ_100)
        getCompanyNews("TSLA", "2021-03-15", "2021-03-21")
        getExchangeRate(Currency.USD)
    }
    fun initModels() {
        companyInfoModel = ViewModelProvider(this).get(CompanyInfoViewModel::class.java)
        companyInfoModel.getCompanyInfoObserver().observe(this, Observer<CompanyInfoModel> { it ->
            if(it != null) {
                Helper.companyInfoToStock(it)?.let { it1 -> stockList.add(it1) }
                Helper.companyInfoToStock(it)?.let { it1 -> retrieveList(it1) }
            }
            else {
                Log.e("$TAG [Company Info Model]", "Error in fetching data")
            }
        })
        incideConstituensModel = ViewModelProvider(this).get(IndicesConstituenceViewModel::class.java)
        incideConstituensModel.getConstituenceObserver().observe(this, Observer<IndicesConstituensModel> { it ->
            if(it != null) {
                Log.d(TAG, it.constituents.size.toString())
                it.constituents.forEach {
                    getCompanyInfo(it)
                }
            }
            else {
                Log.e("$TAG [Incide Constituens Model]", "Error in fetching data")
            }
        })
        exchangeRateModel = ViewModelProvider(this).get(ExchangeRateViewModel::class.java)
        exchangeRateModel.getExchangeRateObserver().observe(this, Observer<ExchangeRateModel> { it ->
            if(it != null) {
                Log.d(TAG, it.toString())
            }
            else {
                Log.e("$TAG [Exchange Rate Model]", "Error in fetching data")
            }
        })
        companyNewsModel = ViewModelProvider(this).get(CompanyNewsViewModel::class.java)
        companyNewsModel.getNewsListObserver().observe(this, Observer<CompanyNewsModel> {
            if(it != null) {
                Log.d(TAG, it.toString())
            }
            else {
                Log.e("$TAG [Company News Model]", "Error in fetching data")
            }
        })
    }
    fun getCompanyInfo(ticker: String) {
        companyInfoModel.makeApiCall(ticker)
    }
    fun getIndexConstituens(indice: Indices) {
        incideConstituensModel.makeApiCall(indice.code)
    }
    fun getExchangeRate(exchange: Currency) {
        exchangeRateModel.makeApiCall(exchange.name)
    }
    fun getCompanyNews(ticker: String, from: String, to: String) {
        // TODO("Выдает Error in fetching data")
        companyNewsModel.makeApiCall(ticker, from, to)
    }
    private fun retrieveList(stock: Stock) {
        adapter.apply {
            this.stocks.add(stock)
            notifyDataSetChanged()
        }
    }
}
