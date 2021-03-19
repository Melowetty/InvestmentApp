package com.melowetty.investment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.melowetty.investment.models.CompanyProfileModel
import com.melowetty.investment.models.StockListModel
import com.melowetty.investment.viewmodel.CompanyProfileViewModel
import com.melowetty.investment.viewmodel.SearchActivityViewModel

class SearchActivity : AppCompatActivity() {
    private val TAG = this::class.java.simpleName
    lateinit var search: EditText
    lateinit var viewModel: SearchActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val back = findViewById<ImageView>(R.id.search_back)
        val clear = findViewById<ImageView>(R.id.clear)
        search = findViewById(R.id.search_label)

        back.setOnClickListener {
            val stockView = Intent(this, MainActivity::class.java)
            startActivity(stockView)
        }

        search.addTextChangedListener {
            if (it != null) {
                if (it.isNotEmpty())  {
                    clear.visibility = View.VISIBLE
                    loadAPIData(it.toString())
                }
                else clear.visibility = View.GONE
            }
        }
        search.text

        clear.setOnClickListener {
            search.text = null
            clear.visibility = View.GONE
        }
    }
    fun loadAPIData(input: String) {
        viewModel = ViewModelProvider(this).get(SearchActivityViewModel::class.java)
        viewModel.getStockListObserver().observe(this, Observer<StockListModel> {
            if(it != null) {
                it.result.forEach {
                    getCompanyProfileInfo(it.symbol)
                }
            }
            else {
                Log.e(TAG, "Error in fetching data")
            }
        })
        viewModel.makeApiCall(input)
    }
    fun getCompanyProfileInfo(symbol: String) {
        val viewProfileModel = ViewModelProvider(this).get(CompanyProfileViewModel::class.java)
        viewProfileModel.getCompanyProfileObserver().observe(this, Observer<CompanyProfileModel> {
            if(it != null) {
                Log.d(TAG, it.toString())
            }
            else {
                Log.e(TAG, "Error in fetching data")
            }
        })
        viewProfileModel.makeApiCall(symbol)
    }
}