package com.melowetty.investment.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.melowetty.investment.R
import com.melowetty.investment.adapters.StockAdapter
import com.melowetty.investment.enums.Activities
import com.melowetty.investment.enums.Currency
import com.melowetty.investment.enums.Indices
import com.melowetty.investment.listeners.StockClickListener
import com.melowetty.investment.models.*
import com.melowetty.investment.utils.Helper
import com.melowetty.investment.viewmodels.CompanyNewsViewModel
import com.melowetty.investment.viewmodels.CompanyProfileViewModel
import com.melowetty.investment.viewmodels.ExchangeRateViewModel
import com.melowetty.investment.viewmodels.IndicesConstituenceViewModel

class MainActivity : AppCompatActivity(), StockClickListener {
    private val TAG = this::class.java.simpleName

    private lateinit var favourite: TextView
    private lateinit var stocks: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchBar: TextView
    private lateinit var mShimmerViewContainer: ShimmerFrameLayout

    private lateinit var adapter: StockAdapter

    private lateinit var indiceConstituensModel: IndicesConstituenceViewModel
    private lateinit var exchangeRateModel: ExchangeRateViewModel
    private lateinit var companyNewsModel: CompanyNewsViewModel
    private lateinit var companyProfileModel: CompanyProfileViewModel

    private var target: Activities? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)

        recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter = StockAdapter(arrayListOf(), this)
        recyclerView.adapter = adapter

        favourite = findViewById(R.id.favourite)
        stocks = findViewById(R.id.stocks)
        searchBar = findViewById(R.id.search_bar)
        mShimmerViewContainer = findViewById(R.id.shimmer_view_container)

        target = intent.getSerializableExtra("target") as? Activities

        if(target != null) {
            if(target == Activities.FAVOURITE) {
                Helper.changeCondition(favourite, true)
                Helper.changeCondition(stocks, false)
            }
        }

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

        mShimmerViewContainer.startShimmerAnimation();

        getIndexConstituens(Indices.NASDAQ_100)
        getCompanyNews("TSLA", "2021-03-15", "2021-03-21")
        getExchangeRate(Currency.USD)
    }
    private fun initModels() {
        indiceConstituensModel =
            ViewModelProvider(this).get(IndicesConstituenceViewModel::class.java)
        indiceConstituensModel
            .getConstituenceObserver()
            .observe(this, Observer<IndicesConstituensModel> { it ->
            if(it != null) {
                getCompanyProfile(it.constituents.joinToString(separator = ","))
            }
            else {
                Log.e("$TAG [Indice Constituens Model]", "Error in fetching data")
            }
        })
        exchangeRateModel =
            ViewModelProvider(this).get(ExchangeRateViewModel::class.java)
        exchangeRateModel
            .getExchangeRateObserver()
            .observe(this, Observer<ExchangeRateModel> { it ->
            if(it != null) {
                Log.d(TAG, it.toString())
            }
            else {
                Log.e("$TAG [Exchange Rate Model]", "Error in fetching data")
            }
        })
        companyNewsModel =
            ViewModelProvider(this).get(CompanyNewsViewModel::class.java)
        companyNewsModel
            .getNewsListObserver()
            .observe(this, Observer<List<CompanyNewsModel>> {
            if(it != null) {
                Log.d(TAG, it.toString())
            }
            else {
                Log.e("$TAG [Company News Model]", "Error in fetching data")
            }
        })
        companyProfileModel =
            ViewModelProvider(this).get(CompanyProfileViewModel::class.java)
        companyProfileModel
            .getCompanyProfileObserver()
            .observe(this, Observer<List<CompanyProfileModel>> {
            if(it != null) {
                retrieveList(Helper.convertModelListToStockList(it))
            }
            else {
                Log.e("$TAG [Company Profile Model]", "Error in fetching data")
            }
        })
    }
    private fun getIndexConstituens(indice: Indices) {
        indiceConstituensModel.makeApiCall(indice.code)
    }
    private fun getExchangeRate(exchange: Currency) {
        exchangeRateModel.makeApiCall(exchange.name)
    }
    private fun getCompanyNews(ticker: String, from: String, to: String) {
        // TODO("Выдает Error in fetching data")
        companyNewsModel.makeApiCall(ticker, from, to)
    }
    private fun getCompanyProfile(ticker: String) {
        companyProfileModel.makeApiCall(ticker)
    }
    private fun retrieveList(stocks: List<Stock>) {
        adapter.apply {
            this.addStocks(stocks)
            mShimmerViewContainer.stopShimmerAnimation()
            mShimmerViewContainer.visibility = View.GONE;
            recyclerView.visibility = View.VISIBLE
            notifyDataSetChanged()
        }
    }

    override fun onStockClick(stock: Stock) {
        startActivity(
            Helper.getStockInfoIntent(this, stock, Activities.MAIN))
    }
}
