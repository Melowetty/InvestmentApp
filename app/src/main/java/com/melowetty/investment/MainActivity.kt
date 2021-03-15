package com.melowetty.investment

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.melowetty.investment.utils.Helper

class MainActivity : AppCompatActivity() {
    private val TAG = this::class.java.simpleName
    private lateinit var favourite: TextView
    private lateinit var stocks: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchBar: TextView
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
            //val stockView = Intent(this, StockView::class.java)
            //startActivity(stockView)
        }
    }

    class fillRecyclerView(val target: RecyclerView, val context: Context) :
        AsyncTask<Void, Void, ArrayList<StockItem>>() {
        private val TAG = this::class.java.simpleName
        override fun doInBackground(vararg params: Void?): ArrayList<StockItem> {
            val stockList = ArrayList<StockItem>()
            val profile = CompanyProfile("YNDX")
            stockList.add(profile.getStockItem())
            val profile2 = CompanyProfile("AAPL")
            stockList.add(profile2.getStockItem())
            return stockList
            // TODO(" Вызов Company Profile выдает Caused by: java.lang.ClassNotFoundException: Didn't find class "java.time.OffsetDateTime". Требует перехода на вебсокеты ")
        }

        override fun onPostExecute(result: ArrayList<StockItem>) {
            target.layoutManager = LinearLayoutManager(context)
            target.adapter = StockAdapter(result, context)
            super.onPostExecute(result)
        }
    }
}
