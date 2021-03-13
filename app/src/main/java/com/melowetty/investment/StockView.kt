package com.melowetty.investment

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.db.williamchart.view.LineChartView


class StockView : AppCompatActivity() {
    private var lineChartView = LineChartView(this)
    private var lineChartValue = TextView(this)
    fun getLineChart(): LineChartView {
        return lineChartView
    }
    fun getLineChartValue(): TextView {
        return lineChartValue
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stock_view)
        lineChartView = findViewById(R.id.lineChart)
        lineChartValue = findViewById(R.id.lineChartValue)

    }
}