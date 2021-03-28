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

    private lateinit var mFavourite: TextView
    private lateinit var mStocks: TextView
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mSearchBar: TextView
    private lateinit var mShimmerViewContainer: ShimmerFrameLayout

    private lateinit var mAdapter: StockAdapter

    private lateinit var indicesConstituentsModel: IndicesConstituentsViewModel
    private lateinit var exchangeRateModel: ExchangeRateViewModel
    private lateinit var companyNewsModel: CompanyNewsViewModel
    private lateinit var companyProfileModel: CompanyProfileViewModel
    private lateinit var favouriteStocksViewModel: FavouriteStocksViewModel

    private var favouriteStocks: List<FavouriteStock> = ArrayList()
    private var stocks: ArrayList<Stock> = ArrayList()

    private var db: AppDatabase? = null

    private var target: Activities? = null

    private val index = Indices.NASDAQ_100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mRecyclerView = findViewById(R.id.recyclerView)

        mRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mAdapter = StockAdapter(arrayListOf(), this)
        mRecyclerView.adapter = mAdapter


        mFavourite = findViewById(R.id.favourite)
        mStocks = findViewById(R.id.stocks)
        mSearchBar = findViewById(R.id.search_bar)
        mShimmerViewContainer = findViewById(R.id.shimmer_view_container)

        target = intent.getSerializableExtra("target") as? Activities

        if(target != null) {
            if(target == Activities.FAVOURITE) {
                Helper.changeCondition(mFavourite, true)
                Helper.changeCondition(mStocks, false)
            }
        }

        mFavourite.setOnClickListener {
            Helper.changeCondition(mFavourite, true)
            Helper.changeCondition(mStocks, false)
            getFavouriteCompanyProfile(Helper.favouriteStocksToString(favouriteStocks))
            mShimmerViewContainer.startShimmer()
            mShimmerViewContainer.visibility = View.VISIBLE
            mRecyclerView.visibility = View.GONE
        }
        mStocks.setOnClickListener {
            Helper.changeCondition(mFavourite, false)
            Helper.changeCondition(mStocks, true)
            mShimmerViewContainer.startShimmer()
            mShimmerViewContainer.visibility = View.VISIBLE
            mRecyclerView.visibility = View.GONE
            getIndexConstituents(index)
        }
        mSearchBar.setOnClickListener {
            val stockView = Intent(this, SearchActivity::class.java)
            startActivity(stockView)
        }

        initDatabase()
        initModels()
        initObservers()

        mShimmerViewContainer.startShimmer();

        getIndexConstituents(index)
        getExchangeRate(Currency.USD)


    }
    private fun initDatabase() {
        db = AppActivity.getDatabase()
    }
    fun initModels() {
        companyProfileModel =
            ViewModelProviders.of(this).get(CompanyProfileViewModel::class.java)

        indicesConstituentsModel =
            ViewModelProviders.of(this).get(IndicesConstituentsViewModel::class.java)

        exchangeRateModel =
            ViewModelProviders.of(this).get(ExchangeRateViewModel::class.java)

        companyNewsModel =
            ViewModelProviders.of(this).get(CompanyNewsViewModel::class.java)

        favouriteStocksViewModel =
            ViewModelProviders.of(this).get(FavouriteStocksViewModel::class.java)
    }
    private fun initObservers() {
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
    private fun getCompanyNews(ticker: String) {
        companyNewsModel.makeApiCall(ticker)
    }
    private fun getCompanyProfile(ticker: String) {
        companyProfileModel.makeApiCall(ticker)
    }
    private fun retrieveList(stocks: List<Stock>) {
        mAdapter.apply {
            this.addStocks(stocks)
            mShimmerViewContainer.stopShimmer()
            mShimmerViewContainer.visibility = View.GONE
            mRecyclerView.visibility = View.VISIBLE
            notifyDataSetChanged()
        }
    }

    override fun onStockClick(stock: Stock) {
        // TODO Нужно сделать сохранение выгруженных акций
        startActivity(
            Helper.getStockInfoIntent(this, stock, Activities.MAIN)
        )
    }
}
