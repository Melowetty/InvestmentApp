package com.melowetty.investment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.db.williamchart.ExperimentalFeature
import com.db.williamchart.slidertooltip.SliderTooltip

class TestFragment : Fragment() {
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ):
        View? = inflater.inflate(R.layout.activity_stock_view, container, false)

    override fun onViewCreated(view: View, saveInstanceState: Bundle?) {

        /**
         * Line Chart
         */
        // TODO
        val lineChart = StockView().getLineChart()
        val lineChartValue = StockView().getLineChartValue()
        lineChart.gradientFillColors =
                intArrayOf(
                        Color.parseColor("#81FFFFFF"),
                        Color.TRANSPARENT
                )
        lineChart.animation.duration = animationDuration
        lineChart.tooltip =
                SliderTooltip().also {
                    it.color = Color.WHITE
                }
        lineChart.onDataPointTouchListener = { index, _, _ ->
            lineChartValue.text =
                    lineSet.toList()[index]
                            .second
                            .toString()
        }
        lineChart.animate(lineSet)
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