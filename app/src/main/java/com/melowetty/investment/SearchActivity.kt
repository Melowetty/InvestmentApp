package com.melowetty.investment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.melowetty.investment.models.CompanyInfoModel
import com.melowetty.investment.models.SearchModel
import com.melowetty.investment.models.Stock
import com.melowetty.investment.utils.Helper
import com.melowetty.investment.viewmodel.CompanyInfoViewModel
import com.melowetty.investment.viewmodel.SearchViewModel

class SearchActivity : AppCompatActivity() {

    private val TAG = this::class.java.simpleName

    private lateinit var search: EditText
    private lateinit var miniRecyclerView: RecyclerView

    private lateinit var adapter: StockAdapter

    private lateinit var searchModel: SearchViewModel
    private lateinit var companyInfoModel: CompanyInfoViewModel

    private var latest = 0L
    private var delay = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        miniRecyclerView = findViewById(R.id.mini_recycler_view)
        miniRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter = StockAdapter(arrayListOf())
        miniRecyclerView.adapter = adapter

        initViewModels()

        val back = findViewById<ImageView>(R.id.search_back)
        val clear = findViewById<ImageView>(R.id.clear)
        val menu = findViewById<ConstraintLayout>(R.id.menu)
        val searchInfo = findViewById<ConstraintLayout>(R.id.search_info)

        search = findViewById(R.id.search_label)

        back.setOnClickListener {
            val stockView = Intent(this, MainActivity::class.java)
            startActivity(stockView)
        }

        search.addTextChangedListener {
            if (it != null) {
                if (it.isNotEmpty())  {
                    clear.visibility = View.VISIBLE
                    menu.visibility = View.VISIBLE
                    searchInfo.visibility = View.GONE
                    miniRecyclerView.visibility = View.VISIBLE

                }
                else {
                    clear.visibility = View.GONE
                    menu.visibility = View.GONE
                    searchInfo.visibility = View.VISIBLE
                    miniRecyclerView.visibility = View.GONE
                    clearResultList()
                }
            }
        }
        search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if(s.toString().isNotEmpty()) filterWithDelay(s.toString())
            }
        })
        clear.setOnClickListener {
            search.text = null
            clear.visibility = View.GONE
        }
    }
    fun initViewModels() {
        searchModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        searchModel.getFindetStocksObserver().observe(this, Observer<SearchModel> {
            if (it != null) {
                it.data.forEach {
                    clearResultList()
                    getCompanyInfo(it.symbol)
                }
            } else {
                Log.e(TAG, "Error in fetching data")
            }
        })
        companyInfoModel = ViewModelProvider(this).get(CompanyInfoViewModel::class.java)
        companyInfoModel.getCompanyInfoObserver().observe(this, Observer<CompanyInfoModel> { it ->
            if(it != null) {
                Helper.companyInfoToStock(it)?.let { it1 -> retrieveList(it1) }
            }
            else {
                Log.e("$TAG [Company Info Model]", "Error in fetching data")
            }
        })
    }
    fun searchStocks(input: String) {
        searchModel.makeApiCall(input)
    }
    fun getCompanyInfo(ticker: String) {
        companyInfoModel.makeApiCall(ticker)
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
    private fun retrieveList(stock: Stock) {
        adapter.apply {
            if(stocks.size == 6) return
            stocks.forEach {
                if(it.symbol == stock.symbol) return
            }
            stocks.add(stock)
            notifyDataSetChanged()
        }
    }
    private fun clearResultList() {
        adapter.apply {
            stocks.clear()
            notifyDataSetChanged()
        }
    }
}