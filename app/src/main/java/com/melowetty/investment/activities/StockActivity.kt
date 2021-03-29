package com.melowetty.investment.activities

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProviders
import com.db.williamchart.slidertooltip.SliderTooltip
import com.db.williamchart.view.LineChartView
import com.melowetty.investment.AppActivity
import com.melowetty.investment.R
import com.melowetty.investment.database.models.FavouriteStock
import com.melowetty.investment.enums.Activities
import com.melowetty.investment.enums.Resolution
import com.melowetty.investment.models.Stock
import com.melowetty.investment.utils.Helper
import com.melowetty.investment.viewmodels.CandlesViewModel
import com.melowetty.investment.viewmodels.FavouriteStocksViewModel


class StockActivity : AppCompatActivity() {

    private val TAG = this::class.java.simpleName

    private lateinit var lineChart: LineChartView

    private lateinit var mName: TextView
    private lateinit var mCompany: TextView
    private lateinit var mCost: TextView
    private lateinit var mChange: TextView
    private lateinit var mDateCost: TextView
    private lateinit var mLineCost: TextView
    private lateinit var mCostLayout: ConstraintLayout

    private lateinit var mFavourite: ImageView

    private lateinit var mBuy: Button

    private lateinit var stock: Stock
    private lateinit var from: Activities
    private lateinit var candles: List<Pair<String, Float>>
    private var selectedResolution: Resolution = Resolution.PER_15MIN
    private lateinit var mSelectedTextView: TextView

    private val favouriteStocksViewModel by lazy { ViewModelProviders.of(this)
        .get(FavouriteStocksViewModel::class.java)}
    private lateinit var candlesViewModel: CandlesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stock)

        val mBack = findViewById<ImageView>(R.id.back)
        val mDay = findViewById<TextView>(R.id.day)
        val mWeek = findViewById<TextView>(R.id.week)
        val mMonth = findViewById<TextView>(R.id.month)
        val mSixMonth = findViewById<TextView>(R.id.sixmonth)
        val mYear = findViewById<TextView>(R.id.year)
        val mFiveYear = findViewById<TextView>(R.id.all)
        mFavourite = findViewById(R.id.favourite_btn)
        lineChart = findViewById(R.id.lineChart)

        stock = intent.getSerializableExtra("stock") as Stock
        from = intent.getSerializableExtra("from") as Activities

        mName = findViewById(R.id.name)
        mCompany = findViewById(R.id.company)
        mCost = findViewById(R.id.cost)
        mChange = findViewById(R.id.difference)
        mBuy = findViewById(R.id.buy)
        mCostLayout = findViewById(R.id.dynamic)
        mDateCost = findViewById(R.id.date)
        mLineCost = findViewById(R.id.line_cost)

        mBack.setOnClickListener {
            from.backToOldActivity(this)
        }

        val db = AppActivity.getDatabase()

        initModels()
        initObservers()
        initGraph()
        initStock()

        mSelectedTextView = mDay

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

        mDay.setOnClickListener {
            mDay.setTextAppearance(R.style.intervalActive)
            mDay.setBackgroundResource(R.drawable.btn_interval_active)
            mSelectedTextView.setTextAppearance(R.style.intervalNotActive)
            mSelectedTextView.setBackgroundResource(R.drawable.btn_interval)
            mSelectedTextView = mDay
            getCandlesFromDay(stock.symbol)
            Log.d(TAG, "${mSelectedTextView.text}")
        }
        mWeek.setOnClickListener {
            mWeek.setTextAppearance(R.style.intervalActive)
            mWeek.setBackgroundResource(R.drawable.btn_interval_active)
            mSelectedTextView.setTextAppearance(R.style.intervalNotActive)
            mSelectedTextView.setBackgroundResource(R.drawable.btn_interval)
            mSelectedTextView = mWeek
            getCandlesFromWeek(stock.symbol)
            Log.d(TAG, "${mSelectedTextView.text}")
        }
        mMonth.setOnClickListener {
            mMonth.setTextAppearance(R.style.intervalActive)
            mMonth.setBackgroundResource(R.drawable.btn_interval_active)
            mSelectedTextView.setTextAppearance(R.style.intervalNotActive)
            mSelectedTextView.setBackgroundResource(R.drawable.btn_interval)
            mSelectedTextView = mMonth
            getCandlesFromMonth(stock.symbol)
            Log.d(TAG, "${mSelectedTextView.text}")
        }
        mSixMonth.setOnClickListener {
            mSixMonth.setTextAppearance(R.style.intervalActive)
            mSixMonth.setBackgroundResource(R.drawable.btn_interval_active)
            mSelectedTextView.setTextAppearance(R.style.intervalNotActive)
            mSelectedTextView.setBackgroundResource(R.drawable.btn_interval)
            mSelectedTextView = mSixMonth
            getCandlesFromSixMonth(stock.symbol)
            Log.d(TAG, "${mSelectedTextView.text}")
        }
        mYear.setOnClickListener {
            mYear.setTextAppearance(R.style.intervalActive)
            mYear.setBackgroundResource(R.drawable.btn_interval_active)
            mSelectedTextView.setTextAppearance(R.style.intervalNotActive)
            mSelectedTextView.setBackgroundResource(R.drawable.btn_interval)
            mSelectedTextView = mYear
            getCandlesFromYear(stock.symbol)
            Log.d(TAG, "${mSelectedTextView.text}")
        }
        mFiveYear.setOnClickListener {
            mFiveYear.setTextAppearance(R.style.intervalActive)
            mFiveYear.setBackgroundResource(R.drawable.btn_interval_active)
            mSelectedTextView.setTextAppearance(R.style.intervalNotActive)
            mSelectedTextView.setBackgroundResource(R.drawable.btn_interval)
            mSelectedTextView = mFiveYear
            getCandlesFrom5Year(stock.symbol)
            Log.d(TAG, "${mSelectedTextView.text}")
        }

    }
    private fun initModels() {
        candlesViewModel =
            ViewModelProviders.of(this).get(CandlesViewModel::class.java)
    }
    private fun initObservers() {
        candlesViewModel
            .getCandlesObserver()
            .observe(this, {
                if (it != null) {
                    candles = Helper.zipCandles(it.t, it.c, selectedResolution, this).toList()
                    lineChart.animate(candles)
                } else {
                    Log.e("$TAG [Candles Model]", "Error in fetching data")
                }
            })
    }
    private fun getCandlesFromDay(ticker: String) {
        selectedResolution = Resolution.PER_15MIN
        candlesViewModel.getCandlesFromDay(ticker)
    }
    private fun getCandlesFromWeek(ticker: String) {
        selectedResolution = Resolution.PER_HOUR
        candlesViewModel.getCandlesFromWeek(ticker)
    }
    private fun getCandlesFromMonth(ticker: String) {
        selectedResolution = Resolution.PER_HOUR
        candlesViewModel.getCandlesFromMonth(ticker)
    }
    private fun getCandlesFromSixMonth(ticker: String) {
        selectedResolution = Resolution.PER_DAY
        candlesViewModel.getCandlesFromSixMonth(ticker)
    }
    private fun getCandlesFromYear(ticker: String) {
        selectedResolution = Resolution.PER_DAY
        candlesViewModel.getCandlesFromYear(ticker)
    }
    private fun getCandlesFrom5Year(ticker: String) {
        selectedResolution = Resolution.PER_WEEK
        candlesViewModel.getCandlesFrom5Year(ticker)
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
            mCostLayout.visibility = View.VISIBLE
            mDateCost.text = candles.toList()[index].first
            mLineCost.text = stock.stockPrice.currency.format(candles.toList()[index].second.toDouble())
        }
    }
    @SuppressLint("SetTextI18n")
    fun initStock() {
        mName.text = stock.symbol

        mCompany.text = Helper.checkLengthLargeCompany(stock.company)

        mCost.text = stock.stockPrice.currency.format(stock.stockPrice.price)
        Helper.formatChangePrice(mChange, stock.stockPrice)
        mBuy.text = "${getString(R.string.buy_btn)} ${stock.stockPrice.currency.format(stock.stockPrice.price)}"

        if(stock.isFavourite) mFavourite.setImageResource(R.drawable.ic_favourite)
        getCandlesFromDay(stock.symbol)
    }

    companion object {
        private const val animationDuration = 1000L
    }

}