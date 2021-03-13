package com.melowetty.investment

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private val TAG = this::class.java.simpleName

    private lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        Log.d(TAG, "IO called")
        fillRecyclerView(recyclerView, this).execute()

        Log.d(TAG, "Adapter selected")

        Log.d(TAG, "IO is call")
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
