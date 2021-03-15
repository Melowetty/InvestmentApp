package com.melowetty.investment

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.db.williamchart.slidertooltip.SliderTooltip
import com.db.williamchart.view.LineChartView


class StockView : AppCompatActivity() {
    private lateinit var lineChart: LineChartView
    private lateinit var lineChartValue: TextView
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stock_view)
        lineChart = findViewById(R.id.lineChart)
        //lineChartValue = findViewById(R.id.lineChartValue)
        lineChart.gradientFillColors =
                intArrayOf(
                        Color.parseColor("#81DCDCDC"),
                        Color.parseColor("#81EBEBEB")
                )
        lineChart.animation.duration = animationDuration
        lineChart.tooltip =
                SliderTooltip().also {
                    it.color = Color.BLACK
                }
        lineChart.onDataPointTouchListener = { index, _, _ ->
            //lineChartValue.text =
              //      lineSet.toList()[index]
                //            .first + " " + lineSet.toList()[index].second.toString()
        }
        lineChart.animate(lineSet)
        // TODO БИБЛИОТЕКА УСПЕШНО ПРОТЕСТИРОВАНА
    }
    companion object {
        private val lineSet = listOf(
                "label1" to 5f,
                "label2" to 4.5f,
                "label3" to 4.7f,
                "label4" to 3.5f,
                "label5" to 3.6f,
                "label6" to 7.5f,
                "label7" to 7.5f,
                "label8" to 10f,
                "label9" to 5f,
                "label10" to 6.5f,
                "label11" to 3f,
                "label12" to 4f
        )

        private const val animationDuration = 1000L
    }
}