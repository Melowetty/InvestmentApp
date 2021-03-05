package com.melowetty.investment

import android.R
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import im.dacer.androidcharts.LineView


class StockView : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stock_view)
        val lineView = findViewById<View>(R.id.line_view) as LineView
        lineView.setDrawDotLine(false) //optional
        lineView.

        lineView.setShowPopup(LineView.SHOW_POPUPS_MAXMIN_ONLY) //optional

        lineView.setBottomTextList(strList)
        lineView.setColorArray(intArrayOf(Color.BLACK, Color.GREEN, Color.GRAY, Color.CYAN))
        lineView.setDataList(dataLists) //or lineView.setFloatDataList(floatDataLists)

    }
}