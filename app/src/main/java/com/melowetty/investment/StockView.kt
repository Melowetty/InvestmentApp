package com.melowetty.investment

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.db.williamchart.view.LineChartView


class StockView : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stock_view)
        val lineChart = findViewById<LineChartView>(R.id.lineChart)

    }
}