package com.melowetty.investment.activities

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.db.williamchart.slidertooltip.SliderTooltip
import com.db.williamchart.view.LineChartView
import com.melowetty.investment.AppActivity
import com.melowetty.investment.R
import com.melowetty.investment.database.models.FavouriteStock
import com.melowetty.investment.enums.Activities
import com.melowetty.investment.models.Stock
import com.melowetty.investment.utils.Helper
import com.melowetty.investment.viewmodels.FavouriteStocksViewModel


class StockActivity : AppCompatActivity() {

    private val TAG = this::class.java.simpleName

    private lateinit var lineChart: LineChartView

    private lateinit var mName: TextView
    private lateinit var mCompany: TextView
    private lateinit var mCost: TextView
    private lateinit var mChange: TextView

    private lateinit var mFavourite: ImageView

    private lateinit var mBuy: Button

    private lateinit var stock: Stock
    private lateinit var from: Activities

    private val favouriteStocksViewModel by lazy { ViewModelProviders.of(this)
        .get(FavouriteStocksViewModel::class.java)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stock)

        val mBack = findViewById<ImageView>(R.id.back)
        mFavourite = findViewById(R.id.favourite_btn)
        lineChart = findViewById(R.id.lineChart)

        stock = intent.getSerializableExtra("stock") as Stock
        from = intent.getSerializableExtra("from") as Activities

        mName = findViewById(R.id.name)
        mCompany = findViewById(R.id.company)
        mCost = findViewById(R.id.cost)
        mChange = findViewById(R.id.difference)
        mBuy = findViewById(R.id.buy)

        mBack.setOnClickListener {
            from.backToOldActivity(this)
        }

        val db = AppActivity.getDatabase()

        mFavourite.setOnClickListener {
            if (!stock.isFavourite) {
                db?.favouriteStockDao()?.insert(FavouriteStock(stock.symbol, stock.company))
                stock.isFavourite = true
                mFavourite.setImageResource(R.drawable.ic_favourite)
            }
            else {
                db?.favouriteStockDao()?.delete(FavouriteStock(stock.symbol, stock.company))
                stock.isFavourite = false
                mFavourite.setImageResource(R.drawable.ic_not_favourite)
            }
            favouriteStocksViewModel.updateFavouriteStocks()
        }

        initStock()
        initGraph()

    }
    private fun initGraph() {
        // TODO Осталось настроитть график и выгрузку новостей
        lineChart.gradientFillColors =
            intArrayOf(
                Color.parseColor("#81DCDCDC"),
                Color.WHITE
            )
        lineChart.animation.duration = animationDuration
        lineChart.tooltip =
            SliderTooltip().also {
                it.color = Color.BLACK
            }
        lineChart.onDataPointTouchListener = { index, x, y ->
            Log.d(TAG, "Coords: $x $y | Index: $index")
            //lineChartValue.text =
            //      lineSet.toList()[index]
            //            .first + " " + lineSet.toList()[index].second.toString()
        }
        lineChart.animate(lineSet)
    }
    @SuppressLint("SetTextI18n")
    fun initStock() {
        mName.text = stock.symbol

        mCompany.text = Helper.checkLengthLargeCompany(stock.company)

        mCost.text = stock.stockPrice.currency.format(stock.stockPrice.price)
        Helper.formatChangePrice(mChange, stock.stockPrice)
        mBuy.text = "${getString(R.string.buy_btn)} ${stock.stockPrice.currency.format(stock.stockPrice.price)}"
        if(stock.isFavourite) mFavourite.setImageResource(R.drawable.ic_favourite)
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