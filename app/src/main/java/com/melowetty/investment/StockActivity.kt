package com.melowetty.investment

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.db.williamchart.slidertooltip.SliderTooltip
import com.db.williamchart.view.LineChartView
import com.melowetty.investment.models.Stock
import com.melowetty.investment.utils.Helper


class StockActivity : AppCompatActivity() {

    private lateinit var lineChart: LineChartView

    private lateinit var lineChartValue: TextView
    private lateinit var name: TextView
    private lateinit var company: TextView
    private lateinit var cost: TextView
    private lateinit var change: TextView

    private lateinit var buy: Button

    private lateinit var stock: Stock
    private lateinit var from: Activities

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stock)

        val back = findViewById<ImageView>(R.id.back)
        lineChart = findViewById(R.id.lineChart)

        stock = intent.getSerializableExtra("stock") as Stock
        from = intent.getSerializableExtra("from") as Activities

        name = findViewById(R.id.name)
        company = findViewById(R.id.company)
        cost = findViewById(R.id.cost)
        change = findViewById(R.id.difference)
        buy = findViewById(R.id.buy)

        //lineChartValue = findViewById(R.id.lineChartValue)

        back.setOnClickListener {
            from.backToOldActivity(this)
        }

        initStock()
        initGraph()

    }
    private fun initGraph() {
        // TODO Осталось настроитть график и выгрузку новостей
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
    }
    @SuppressLint("SetTextI18n")
    fun initStock() {
        name.text = stock.symbol
        company.text = Helper.checkLengthLargeCompany(stock.company)
        cost.text = stock.stockPrice.currency.format(stock.stockPrice.price)
        Helper.formatChangePrice(change, stock.stockPrice)
        buy.text = "${getString(R.string.buy_btn)} ${stock.stockPrice.currency.format(stock.stockPrice.price)}"
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