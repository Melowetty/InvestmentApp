package com.melowetty.investment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.hasFixedSize()

        var stockList = ArrayList<StockItem>()
        stockList.add(StockItem("АУФ","АУФ 2",null, 263.0, 2586.0, true))
        val adapter = StockAdapter(stockList, this)
        recyclerView.adapter = adapter


    }
}