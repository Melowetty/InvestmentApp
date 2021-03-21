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
import com.melowetty.investment.viewmodel.*

class MainActivity : AppCompatActivity() {
    private val TAG = this::class.java.simpleName

    private lateinit var favourite: TextView
    private lateinit var stocks: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchBar: TextView
    private lateinit var priceModel: PriceViewModel
    private lateinit var profileModel: CompanyProfileViewModel
    private lateinit var incideConstituensModel: IndicesConstituenceViewModel
    private lateinit var exchangeRateModel: ExchangeRateViewModel
    private lateinit var companyNewsModel: CompanyNewsViewModel
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
        }
        searchBar.setOnClickListener {
            val stockView = Intent(this, SearchActivity::class.java)
            startActivity(stockView)
        }

        initModels()

        getRealTimePrice("AAPL")
        getCompanyProfile("AAPL")
        getIndexConstituens(Indices.DOW_JONES)
        getExchangeRate(Currency.USD)
        getCompanyNews("MSFT", "13.03.2021", "20.03.2021")
    }
    fun initModels() {
        priceModel = ViewModelProvider(this).get(PriceViewModel::class.java)
        priceModel.getPriceObserver().observe(this, Observer<RealTimePriceModel> { it ->
            if(it != null) {
                Log.d(TAG, it.toString())
            }
            else {
                Log.e(TAG, "Error in fetching data")
            }
        })
        profileModel = ViewModelProvider(this).get(CompanyProfileViewModel::class.java)
        profileModel.getCompanyProfileObserver().observe(this, Observer<CompanyProfileModel> {
            if(it != null) {
                Log.d(TAG, it.toString())
            }
            else {
                Log.e(TAG, "Error in fetching data")
            }
        })
        incideConstituensModel = ViewModelProvider(this).get(IndicesConstituenceViewModel::class.java)
        incideConstituensModel.getConstituenceObserver().observe(this, Observer<IndicesConstituensModel> { it ->
            if(it != null) {
                Log.d(TAG, it.toString())
            }
            else {
                Log.e(TAG, "Error in fetching data")
            }
        })
        exchangeRateModel = ViewModelProvider(this).get(ExchangeRateViewModel::class.java)
        exchangeRateModel.getExchangeRateObserver().observe(this, Observer<ExchangeRateModel> { it ->
            if(it != null) {
                Log.d(TAG, it.toString())
            }
            else {
                Log.e(TAG, "Error in fetching data")
            }
        })
        companyNewsModel = ViewModelProvider(this).get(CompanyNewsViewModel::class.java)
        companyNewsModel.getNewsListObserver().observe(this, Observer<CompanyNewsModel> {
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
    fun getCompanyProfile(symbol: String) {
        profileModel.makeApiCall(symbol)
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
}
