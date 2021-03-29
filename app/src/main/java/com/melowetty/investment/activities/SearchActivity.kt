package com.melowetty.investment.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.melowetty.investment.AppActivity
import com.melowetty.investment.R
import com.melowetty.investment.adapters.RequestsAdapter
import com.melowetty.investment.adapters.StockAdapter
import com.melowetty.investment.database.AppDatabase
import com.melowetty.investment.database.models.SearchedItem
import com.melowetty.investment.enums.Activities
import com.melowetty.investment.listeners.ItemClickListener
import com.melowetty.investment.listeners.StockClickListener
import com.melowetty.investment.models.Stock
import com.melowetty.investment.utils.Helper
import com.melowetty.investment.viewmodels.FindStockViewModel
import com.melowetty.investment.viewmodels.ProfileViewModel
import com.melowetty.investment.viewmodels.SearchHistoryViewModel


class SearchActivity : AppCompatActivity(), StockClickListener, ItemClickListener {

    private val TAG = this::class.java.simpleName

    private lateinit var mSearch: EditText
    private lateinit var mMiniRecyclerView: RecyclerView
    private lateinit var mSearchRecyclerView: RecyclerView
    private lateinit var mPopularityRecyclerView: RecyclerView
    private lateinit var mShimmerViewContainer: ShimmerFrameLayout
    private lateinit var mNotFound: TextView
    private lateinit var mMenu: ConstraintLayout
    private lateinit var mError: TextView

    private lateinit var mAdapter: StockAdapter
    private lateinit var mRequestsAdapter: RequestsAdapter

    private lateinit var searchModel: FindStockViewModel
    private lateinit var companyProfileModel: ProfileViewModel
    private lateinit var searchHistoryModel: SearchHistoryViewModel

    private var db: AppDatabase? = null
    private var historySearches: List<SearchedItem> = ArrayList()

    private var latest = 0L
    private var delay = 2500

    private var isShowError = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        mMiniRecyclerView = findViewById(R.id.mini_recycler_view)
        mSearchRecyclerView = findViewById(R.id.searched_recycler)
        mPopularityRecyclerView = findViewById(R.id.popular_recycler)
        mMenu = findViewById(R.id.menu)
        mSearch = findViewById(R.id.search_label)
        mShimmerViewContainer = findViewById(R.id.shimmer_view_container)
        mNotFound = findViewById(R.id.result_not_found)
        mError = findViewById(R.id.search_error)

        val mSearchInfo = findViewById<ConstraintLayout>(R.id.search_info)
        val mClear = findViewById<ImageView>(R.id.clear)
        val mBack = findViewById<ImageView>(R.id.search_back)

        initDatabse()

        initModels()
        initObservers()

        initPopularityRecyclerView()
        initSearchedRecyclerView()
        initMiniRecyclerView()

        mBack.setOnClickListener {
            val stockView = Intent(this, MainActivity::class.java)
            startActivity(stockView)
        }

        mSearch.addTextChangedListener {
            if (it != null) {
                if (it.isNotEmpty())  {
                    mClear.visibility = View.VISIBLE
                    mMenu.visibility = View.VISIBLE
                    mSearchInfo.visibility = View.GONE
                    mShimmerViewContainer.visibility = View.VISIBLE
                    mShimmerViewContainer.startShimmer();

                }
                else {
                    mClear.visibility = View.GONE
                    mMenu.visibility = View.GONE
                    mSearchInfo.visibility = View.VISIBLE
                    mMiniRecyclerView.visibility = View.GONE
                    mShimmerViewContainer.stopShimmer()
                    mShimmerViewContainer.visibility = View.GONE
                    mNotFound.visibility = View.GONE
                    clearResultList()
                }
            }
        }
        mSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearResultList()
            }

            override fun afterTextChanged(s: Editable) {
                if (s.toString().isNotEmpty()) filterWithDelay(s.toString())
            }
        })
        mClear.setOnClickListener {
            mSearch.text = null
            mClear.visibility = View.GONE
            mNotFound.visibility = View.GONE
        }

    }
    fun initPopularityRecyclerView() {
        mPopularityRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val list = Helper.convertStringListToSearchedItem(
            arrayListOf("Yandex", "Nvidia", "Microsoft", "Tesla", "Apple", "McDonalds", "MasterCard", "Facebook", "Visa", "Amazon", "AMD", "Intel", "Ebay", "Google", "Netflix"))
        mRequestsAdapter = RequestsAdapter(list, this)
        mPopularityRecyclerView.adapter = mRequestsAdapter
    }
    private fun showErrorMessage() {
        isShowError = true
        mError.visibility = View.VISIBLE
        mMiniRecyclerView.visibility = View.GONE
        mSearchRecyclerView.visibility = View.GONE
        mPopularityRecyclerView.visibility = View.GONE
        mShimmerViewContainer.stopShimmer()
        mShimmerViewContainer.visibility = View.GONE
        mMenu.visibility = View.GONE
    }
    private fun hideErrorMessage() {
        isShowError = false
        mError.visibility = View.GONE
    }
    fun initDatabse() {
        db = AppActivity.getDatabase()
    }
    fun initModels() {
        companyProfileModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)
        searchModel =
            ViewModelProvider(this).get(FindStockViewModel::class.java)
        searchHistoryModel =
            ViewModelProviders.of(this).get(SearchHistoryViewModel::class.java)
    }
    private fun initObservers() {
        searchModel
            .getFindStocksObserver()
            .observe(this, {
                if (it != null) {
                    if(isShowError) hideErrorMessage()
                    clearResultList()
                    getCompanyProfile(
                        Helper.convertModelListToStringList(it).joinToString(separator = ",")
                    )

                } else {
                    showErrorMessage()
                }
            })

        companyProfileModel
            .getCompanyProfileObserver()
            .observe(this, {
                if (it != null) {
                    if(isShowError) hideErrorMessage()
                    retrieveList(Helper.convertModelListToStockList(it, arrayListOf()))
                } else {
                    showErrorMessage()
                }
            })
        searchHistoryModel.historySearch.observe(this, {
            it?.let {
                historySearches = it
                updateSearchedRecycler()
            }
        })
    }
    fun initMiniRecyclerView() {
        mMiniRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mAdapter = StockAdapter(arrayListOf(), this)
        mMiniRecyclerView.adapter = mAdapter
    }
    fun initSearchedRecyclerView() {
        mSearchRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mRequestsAdapter = RequestsAdapter(arrayListOf(), this)
        mSearchRecyclerView.adapter = mRequestsAdapter
    }
    private fun searchStocks(input: String) {
        searchModel.makeApiCall(input)
        db?.historySearchDao()?.insert(SearchedItem(input))
        searchHistoryModel.updateSearchedStocks()
    }
    private fun getCompanyProfile(ticker: String) {
        companyProfileModel.makeApiCall(ticker)
    }
    fun filterWithDelay(s: String) {
        latest = System.currentTimeMillis()
        val h = Handler(Looper.getMainLooper())
        val r = Runnable {
            if (System.currentTimeMillis() - delay > latest)
                searchStocks(input = s)
        }
        h.postDelayed(
            r,
            (delay + 50).toLong()
        )
    }
    private fun retrieveList(stocks: List<Stock>) {

        if(stocks.isNullOrEmpty()) {
            mNotFound.visibility = View.VISIBLE
            mShimmerViewContainer.visibility = View.GONE
            mMenu.visibility = View.GONE
            mShimmerViewContainer.stopShimmer()
        }
        else mAdapter.apply {
            mShimmerViewContainer.stopShimmer()
            mShimmerViewContainer.visibility = View.GONE
            mMiniRecyclerView.visibility = View.VISIBLE
            this.addStocks(stocks)
            notifyDataSetChanged()
        }
    }
    private fun updateSearchedRecycler() {
        mRequestsAdapter.apply {
            addTickers(historySearches)
            notifyDataSetChanged()
        }
    }
    private fun clearResultList() {
        mAdapter.apply {
            stocks.clear()
            notifyDataSetChanged()
        }
    }

    override fun onStockClick(stock: Stock) {
        startActivity(
            Helper.getStockInfoIntent(this, stock, Activities.SEARCH)
        )
    }

    override fun onItemClick(ticker: SearchedItem) {
        mSearch.setText(ticker.ticker)
    }
}