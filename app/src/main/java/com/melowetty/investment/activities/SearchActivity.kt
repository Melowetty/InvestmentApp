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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.melowetty.investment.AppActivity
import com.melowetty.investment.R
import com.melowetty.investment.adapters.RequestsAdapter
import com.melowetty.investment.adapters.StocksAdapter
import com.melowetty.investment.database.AppDatabase
import com.melowetty.investment.database.models.FoundTicker
import com.melowetty.investment.enums.Activities
import com.melowetty.investment.listeners.ItemClickListener
import com.melowetty.investment.listeners.StockClickListener
import com.melowetty.investment.models.Stock
import com.melowetty.investment.utils.Helper
import com.melowetty.investment.viewmodels.FindStockViewModel
import com.melowetty.investment.viewmodels.ProfileViewModel
import com.melowetty.investment.viewmodels.SearchHistoryViewModel


class SearchActivity : AppCompatActivity(), StockClickListener, ItemClickListener {

    private lateinit var tvError: TextView
    private lateinit var tvNotFound: TextView
    private lateinit var etSearch: EditText
    private lateinit var rvStocks: RecyclerView
    private lateinit var rvSearchHistory: RecyclerView
    private lateinit var rvPopularity: RecyclerView
    private lateinit var clMenu: ConstraintLayout
    private lateinit var sfl: ShimmerFrameLayout

    private lateinit var stocksAdapter: StocksAdapter
    private lateinit var requestsAdapter: RequestsAdapter

    private lateinit var searchModel: FindStockViewModel
    private lateinit var companyProfileModel: ProfileViewModel
    private lateinit var searchHistoryModel: SearchHistoryViewModel

    private var db: AppDatabase? = null
    private var searchHistory: List<FoundTicker> = ArrayList()

    private var latest = 0L
    private var delay = 2500

    private var isShowError = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        rvStocks = findViewById(R.id.mini_recycler_view)
        rvSearchHistory = findViewById(R.id.searched_recycler)
        rvPopularity = findViewById(R.id.popular_recycler)
        clMenu = findViewById(R.id.menu)
        etSearch = findViewById(R.id.search_label)
        sfl = findViewById(R.id.shimmer_view_container)
        tvNotFound = findViewById(R.id.result_not_found)
        tvError = findViewById(R.id.search_error)

        val clSearchInfo = findViewById<ConstraintLayout>(R.id.search_info)
        val ivClear = findViewById<ImageView>(R.id.clear)
        val ivBack = findViewById<ImageView>(R.id.search_back)

        initDatabse()

        initModels()
        initObservers()

        initPopularityRecyclerView()
        initSearchedRecyclerView()
        initMiniRecyclerView()

        ivBack.setOnClickListener {
            val stockView = Intent(this, MainActivity::class.java)
            startActivity(stockView)
        }

        etSearch.addTextChangedListener {
            if (it != null) {
                if (it.isNotEmpty())  {
                    ivClear.visibility = View.VISIBLE
                    clMenu.visibility = View.VISIBLE
                    clSearchInfo.visibility = View.GONE
                    sfl.visibility = View.VISIBLE
                    sfl.startShimmer();

                }
                else {
                    ivClear.visibility = View.GONE
                    clMenu.visibility = View.GONE
                    clSearchInfo.visibility = View.VISIBLE
                    rvStocks.visibility = View.GONE
                    sfl.stopShimmer()
                    sfl.visibility = View.GONE
                    tvNotFound.visibility = View.GONE
                    clearResultList()
                }
            }
        }
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearResultList()
            }

            override fun afterTextChanged(s: Editable) {
                if (s.toString().isNotEmpty()) filterWithDelay(s.toString())
            }
        })
        ivClear.setOnClickListener {
            etSearch.text = null
            ivClear.visibility = View.GONE
            tvNotFound.visibility = View.GONE
        }

    }
    private fun initPopularityRecyclerView() {
        rvPopularity.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val list = Helper.convertStringListToSearchedItem(
            arrayListOf("Yandex", "Nvidia", "Microsoft", "Tesla", "Apple", "McDonalds", "MasterCard", "Facebook", "Visa", "Amazon", "AMD", "Intel", "Ebay", "Google", "Netflix"))
        requestsAdapter = RequestsAdapter(list, this)
        rvPopularity.adapter = requestsAdapter
    }
    private fun showErrorMessage() {
        isShowError = true
        tvError.visibility = View.VISIBLE
        rvStocks.visibility = View.GONE
        rvSearchHistory.visibility = View.GONE
        rvPopularity.visibility = View.GONE
        sfl.stopShimmer()
        sfl.visibility = View.GONE
        clMenu.visibility = View.GONE
    }
    private fun hideErrorMessage() {
        isShowError = false
        tvError.visibility = View.GONE
    }
    private fun initDatabse() {
        db = AppActivity.getDatabase()
    }
    private fun initModels() {
        companyProfileModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)
        searchModel =
            ViewModelProvider(this).get(FindStockViewModel::class.java)
        searchHistoryModel =
            ViewModelProvider(this).get(SearchHistoryViewModel::class.java)
    }
    private fun initObservers() {
        searchModel
            .getFindStocksObserver()
            .observe(this) {
                if (it != null) {
                    if (isShowError) hideErrorMessage()
                    clearResultList()
                    getCompanyProfile(
                        Helper.convertModelListToStringList(it).joinToString(separator = ",")
                    )

                } else {
                    showErrorMessage()
                }
            }

        companyProfileModel
            .getCompanyProfileObserver()
            .observe(this) {
                if (it != null) {
                    if (isShowError) hideErrorMessage()
                    retrieveList(Helper.convertModelListToStockList(it, arrayListOf()))
                } else {
                    showErrorMessage()
                }
            }
        searchHistoryModel.historySearch.observe(this) {
            it?.let {
                searchHistory = it
                updateSearchedRecycler()
            }
        }
    }
    private fun initMiniRecyclerView() {
        rvStocks.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        stocksAdapter = StocksAdapter(arrayListOf(), this)
        rvStocks.adapter = stocksAdapter
    }
    private fun initSearchedRecyclerView() {
        rvSearchHistory.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        requestsAdapter = RequestsAdapter(arrayListOf(), this)
        rvSearchHistory.adapter = requestsAdapter
    }
    private fun searchStocks(input: String) {
        searchModel.makeApiCall(input)
        db?.foundTickersDao()?.insert(FoundTicker(input))
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
            tvNotFound.visibility = View.VISIBLE
            sfl.visibility = View.GONE
            clMenu.visibility = View.GONE
            sfl.stopShimmer()
        }
        else stocksAdapter.apply {
            sfl.stopShimmer()
            sfl.visibility = View.GONE
            rvStocks.visibility = View.VISIBLE
            this.addStocks(stocks)
            notifyDataSetChanged()
        }
    }
    private fun updateSearchedRecycler() {
        requestsAdapter.apply {
            addTickers(searchHistory)
            notifyDataSetChanged()
        }
    }
    private fun clearResultList() {
        stocksAdapter.apply {
            stocks.clear()
            notifyDataSetChanged()
        }
    }

    override fun onStockClick(stock: Stock) {
        startActivity(
            Helper.getStockInfoIntent(this, stock, Activities.SEARCH)
        )
    }

    override fun onItemClick(ticker: FoundTicker) {
        etSearch.setText(ticker.ticker)
    }
}