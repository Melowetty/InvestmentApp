package com.melowetty.investment.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.melowetty.investment.AppActivity
import com.melowetty.investment.R
import com.melowetty.investment.adapters.StockAdapter
import com.melowetty.investment.database.AppDatabase
import com.melowetty.investment.database.models.FavouriteStock
import com.melowetty.investment.enums.Activities
import com.melowetty.investment.enums.Currency
import com.melowetty.investment.enums.Indices
import com.melowetty.investment.listeners.StockClickListener
import com.melowetty.investment.models.*
import com.melowetty.investment.utils.Helper
import com.melowetty.investment.viewmodels.*


class MainActivity : AppCompatActivity(), StockClickListener {
    private val TAG = this::class.java.simpleName

    private lateinit var favourite: TextView
    private lateinit var stocks: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchBar: TextView
    private lateinit var mShimmerViewContainer: ShimmerFrameLayout

    private lateinit var adapter: StockAdapter

    private val indicesConstituentsModel by lazy { ViewModelProviders.of(this)
        .get(IndicesConstituentsViewModel::class.java)}

    private val exchangeRateModel by lazy { ViewModelProviders.of(this)
        .get(ExchangeRateViewModel::class.java)}

    private val companyNewsModel by lazy { ViewModelProviders.of(this)
        .get(CompanyNewsViewModel::class.java)}

    private val companyProfileModel by lazy { ViewModelProviders.of(this)
        .get(CompanyProfileViewModel::class.java)}

    private val favouriteStocksViewModel by lazy { ViewModelProviders.of(this)
        .get(FavouriteStocksViewModel::class.java)}

    private var favouriteStocks: List<FavouriteStock> = ArrayList()

    private var db: AppDatabase? = null

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
            getFavouriteCompanyProfile(Helper.favouriteStocksToString(favouriteStocks))
            mShimmerViewContainer.startShimmer()
            mShimmerViewContainer.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
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

        mShimmerViewContainer.startShimmer();

        getIndexConstituents(Indices.SP_500)
        getExchangeRate(Currency.USD)

        initDatabase()

    }
    private fun initDatabase() {
        db = AppActivity.getDatabase()
    }
    private fun initModels() {
        favouriteStocksViewModel.favouriteStocks.observe(this, {
            it?.let {
                favouriteStocks = it
            }
        })

        indicesConstituentsModel
            .getConstituentsObserver()
            .observe(this, { it ->
                if (it != null) {
                    getCompanyProfile(it.constituents.joinToString(separator = ","))
                } else {
                    Log.e("$TAG [Indices Constituents Model]", "Error in fetching data")
                }
            })

        exchangeRateModel
            .getExchangeRateObserver()
            .observe(this, { it ->
                if (it != null) {
                    Log.d(TAG, it.toString())
                } else {
                    Log.e("$TAG [Exchange Rate Model]", "Error in fetching data")
                }
            })

        companyNewsModel
            .getNewsListObserver()
            .observe(this, {
                if (it != null) {
                    Log.d(TAG, it.toString())
                } else {
                    Log.e("$TAG [Company News Model]", "Error in fetching data")
                }
            })

        companyProfileModel
            .getCompanyProfileObserver()
            .observe(this, {
                if (it != null) {
                    retrieveList(Helper.convertModelListToStockList(it,
                        favouriteStocks))
                } else {
                    Log.e("$TAG [Company Profile Model]", "Error in fetching data")
                }
            })
    }
    private fun getFavouriteCompanyProfile(favourites: List<String>) {
        getCompanyProfile(favourites.joinToString(","))
    }
    private fun getIndexConstituents(indice: Indices) {
        indicesConstituentsModel.makeApiCall(indice.code)
    }
    private fun getExchangeRate(exchange: Currency) {
        exchangeRateModel.makeApiCall(exchange.name)
    }
    private fun getCompanyNews(ticker: String, from: String, to: String) {
        companyNewsModel.makeApiCall(ticker, from, to)
    }
    private fun getCompanyProfile(ticker: String) {
        companyProfileModel.makeApiCall(ticker)
    }
    private fun retrieveList(stocks: List<Stock>) {
        adapter.apply {
            this.addStocks(stocks)
            mShimmerViewContainer.stopShimmer()
            mShimmerViewContainer.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            notifyDataSetChanged()
        }
    }

    override fun onStockClick(stock: Stock) {
        startActivity(
            Helper.getStockInfoIntent(this, stock, Activities.MAIN)
        )
    }
}
