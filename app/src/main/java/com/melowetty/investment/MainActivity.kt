package com.melowetty.investment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        var stockList = ArrayList<StockItem>()
        stockList.add(StockItem("YNDX","Yandex Inc.","", 263.0, 25.0, true))
        recyclerView.adapter = StockAdapter(stockList, this)


    }
}