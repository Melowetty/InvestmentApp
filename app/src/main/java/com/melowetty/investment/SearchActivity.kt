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
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.melowetty.investment.models.StockListModel
import com.melowetty.investment.viewmodel.SearchActivityViewModel

class SearchActivity : AppCompatActivity() {
    private val TAG = this::class.java.simpleName
    private lateinit var search: EditText
    private lateinit var searchModel: SearchActivityViewModel

    private var latest = 0L
    private var delay = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        initViewModels()

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
                }
                else clear.visibility = View.GONE
            }
        }
        search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                filterWithDelay(s.toString())
            }
        })
        clear.setOnClickListener {
            search.text = null
            clear.visibility = View.GONE
        }
    }
    fun initViewModels() {
        searchModel = ViewModelProvider(this).get(SearchActivityViewModel::class.java)
        searchModel.getStockListObserver().observe(this, Observer<StockListModel> {
            if (it != null) {
                it.result.forEach {
                    Log.d(TAG, it.toString())
                }
            } else {
                Log.e(TAG, "Error in fetching data")
            }
        })
    }
    fun searchStocks(input: String) {
        searchModel.makeApiCall(input)
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
}