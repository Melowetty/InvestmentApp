package com.melowetty.investment.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.db.williamchart.slidertooltip.SliderTooltip
import com.db.williamchart.view.LineChartView
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.melowetty.investment.AppActivity
import com.melowetty.investment.R
import com.melowetty.investment.adapters.NewsAdapter
import com.melowetty.investment.database.models.FavoriteStock
import com.melowetty.investment.enums.Activities
import com.melowetty.investment.enums.Interval
import com.melowetty.investment.enums.Resolution
import com.melowetty.investment.listeners.NewsClickListener
import com.melowetty.investment.models.NewsModel
import com.melowetty.investment.models.Stock
import com.melowetty.investment.utils.Helper
import com.melowetty.investment.viewmodels.CandlesViewModel
import com.melowetty.investment.viewmodels.FavoriteStocksViewModel
import com.melowetty.investment.viewmodels.NewsViewModel


class StockActivity : AppCompatActivity(), NewsClickListener {

    private lateinit var tvName: TextView
    private lateinit var tvCompany: TextView
    private lateinit var tvCost: TextView
    private lateinit var tvChange: TextView
    private lateinit var tvDateCost: TextView
    private lateinit var tvLineCost: TextView
    private lateinit var tvError: TextView
    private lateinit var tvChart: TextView
    private lateinit var tvNews: TextView
    private lateinit var ivFavourite: ImageView
    private lateinit var btnBuy: Button
    private lateinit var rvNews: RecyclerView
    private lateinit var clIntervals: ConstraintLayout
    private lateinit var clCost: ConstraintLayout
    private lateinit var sflNews: ShimmerFrameLayout
    private lateinit var lcv: LineChartView
    private lateinit var pb: CircularProgressIndicator


    private lateinit var stock: Stock
    private lateinit var from: Activities
    private lateinit var mNewsAdapter: NewsAdapter
    private lateinit var candles: List<Pair<String, Float>>
    private var selectedResolution: Resolution = Resolution.PER_15MIN
    private lateinit var selectedInterval: TextView
    private lateinit var selectedMenu: TextView

    private val favouriteStocksViewModel by lazy { ViewModelProvider(this)
        .get(FavoriteStocksViewModel::class.java)}
    private lateinit var candlesViewModel: CandlesViewModel
    private lateinit var companyNewsModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stock)

        val ivBack = findViewById<ImageView>(R.id.back)
        val tvDay = findViewById<TextView>(R.id.day)
        val tvWeek = findViewById<TextView>(R.id.week)
        val tvMonth = findViewById<TextView>(R.id.month)
        val tvSixMonth = findViewById<TextView>(R.id.sixmonth)
        val tvYear = findViewById<TextView>(R.id.year)
        val tvFiveYear = findViewById<TextView>(R.id.all)

        ivFavourite = findViewById(R.id.favourite_btn)
        lcv = findViewById(R.id.lineChart)
        tvError = findViewById(R.id.stock_error)
        tvChart = findViewById(R.id.chart)
        tvNews = findViewById(R.id.news)
        rvNews = findViewById(R.id.news_recyclerView)
        clIntervals = findViewById(R.id.intervals)
        sflNews = findViewById(R.id.news_shimmer_container)
        pb = findViewById(R.id.progress_bar_chart)


        stock = intent.getSerializableExtra("stock") as Stock
        from = intent.getSerializableExtra("from") as Activities

        tvName = findViewById(R.id.name)
        tvCompany = findViewById(R.id.company)
        tvCost = findViewById(R.id.cost)
        tvChange = findViewById(R.id.difference)
        btnBuy = findViewById(R.id.buy)
        clCost = findViewById(R.id.dynamic)
        tvDateCost = findViewById(R.id.date)
        tvLineCost = findViewById(R.id.line_cost)

        ivBack.setOnClickListener {
            from.backToOldActivity(this)
        }

        val db = AppActivity.getDatabase()

        initModels()
        initObservers()
        initGraph()
        initStock()
        initNewsRecyclerView()

        selectedInterval = tvDay
        selectedMenu = tvChart

        tvChart.setOnClickListener {
            Helper.changeCondition(tvChart, true)
            Helper.changeCondition(selectedMenu, false)
            selectedMenu = tvChart
            showGraph()
        }
        tvNews.setOnClickListener {
            Helper.changeCondition(tvNews, true)
            Helper.changeCondition(selectedMenu, false)
            selectedMenu = tvNews
            showNews()
        }
        ivFavourite.setOnClickListener {
            if (!stock.isFavourite) {
                db?.favoriteStockDao()?.insert(FavoriteStock(stock.symbol, stock.company))
                stock.isFavourite = true
                ivFavourite.setImageResource(R.drawable.ic_favourite)
            }
            else {
                db?.favoriteStockDao()?.delete(FavoriteStock(stock.symbol, stock.company))
                stock.isFavourite = false
                ivFavourite.setImageResource(R.drawable.ic_not_favourite)
            }
            favouriteStocksViewModel.updateFavouriteStocks()
        }

        tvDay.setOnClickListener {
            getCandles(tvDay, Interval.DAY)
        }
        tvWeek.setOnClickListener {
            getCandles(tvWeek, Interval.WEEK)
        }
        tvMonth.setOnClickListener {
            getCandles(tvMonth, Interval.MONTH)
        }
        tvSixMonth.setOnClickListener {
            getCandles(tvSixMonth, Interval.SIX_MONTH)
        }
        tvYear.setOnClickListener {
            getCandles(tvYear, Interval.YEAR)
        }
        tvFiveYear.setOnClickListener {
            getCandles(tvFiveYear, Interval.FIVE_YEAR)
        }

    }
    private fun showNews() {
        btnBuy.visibility = View.GONE
        lcv.visibility = View.GONE
        tvCost.visibility = View.GONE
        tvChange.visibility = View.GONE
        clIntervals.visibility = View.GONE
        clCost.visibility = View.INVISIBLE
        Helper.startShimmer(sflNews)
        getCompanyNews(stock.symbol)
        hideErrorMessage(false)
    }
    private fun showGraph() {
        btnBuy.visibility = View.VISIBLE
        lcv.visibility = View.VISIBLE
        tvCost.visibility = View.VISIBLE
        tvChange.visibility = View.VISIBLE
        clIntervals.visibility = View.VISIBLE
        rvNews.visibility = View.GONE
        hideErrorMessage(true)
    }
    private fun showErrorMessage() {
        tvError.visibility = View.VISIBLE
        lcv.visibility = View.GONE
    }
    private fun hideErrorMessage(bool: Boolean) {
        tvError.visibility = View.GONE
        if(bool) lcv.visibility = View.VISIBLE
    }
    private fun initModels() {
        candlesViewModel =
            ViewModelProvider(this).get(CandlesViewModel::class.java)
        companyNewsModel =
            ViewModelProvider(this).get(NewsViewModel::class.java)
    }
    private fun initObservers() {
        candlesViewModel
            .getCandlesObserver()
            .observe(this) {
                if (it != null) {
                    candles = Helper.zipCandles(it.t, it.c, selectedResolution, this).toList()
                    lcv.animate(candles)
                    stopRefreshGraph(true)
                } else {
                    showErrorMessage()
                    stopRefreshGraph(false)
                }
            }
        companyNewsModel
            .getNewsListObserver()
            .observe(this) {
                if (it != null) {
                    updateNewsAdapter(it)
                } else {
                    showErrorMessage()
                }
            }
    }
    private fun initNewsRecyclerView() {
        rvNews.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mNewsAdapter = NewsAdapter(arrayListOf(), this)
        rvNews.adapter = mNewsAdapter
    }
    private fun updateNewsAdapter(news: List<NewsModel>) {
        mNewsAdapter.apply {
            rvNews.visibility = View.VISIBLE
            addNews(news)
            notifyDataSetChanged()
            Helper.stopShimmer(sflNews)
        }
    }
    private fun getCompanyNews(ticker: String) {
        companyNewsModel.makeApiCall(ticker)
    }
    private fun startRefreshGraph() {
        pb.visibility = View.VISIBLE
        lcv.visibility = View.INVISIBLE
    }
    private fun stopRefreshGraph(bool: Boolean) {
        pb.visibility = View.INVISIBLE
        if(bool) lcv.visibility = View.VISIBLE
    }
    private fun getCandles(interval: Interval) {
        selectedResolution = interval.resolution
        candlesViewModel.getCandles(stock.symbol, interval)
        clCost.visibility = View.INVISIBLE
        hideErrorMessage(true)
        startRefreshGraph()
    }
    private fun getCandles(textView: TextView, interval: Interval) {
        textView.setTextAppearance(R.style.intervalActive)
        textView.setBackgroundResource(R.drawable.btn_interval_active)
        selectedInterval.setTextAppearance(R.style.intervalNotActive)
        selectedInterval.setBackgroundResource(R.drawable.btn_interval)
        selectedInterval = textView

        selectedResolution = interval.resolution
        candlesViewModel.getCandles(stock.symbol, interval)

        clCost.visibility = View.INVISIBLE
        hideErrorMessage(true)
        startRefreshGraph()
    }
    private fun initGraph() {
        lcv.gradientFillColors =
            intArrayOf(
                Color.parseColor("#81DCDCDC"),
                Color.WHITE
            )
        lcv.animation.duration = animationDuration
        lcv.tooltip =
            SliderTooltip().also {
                it.color = Color.BLACK
            }
        lcv.onDataPointTouchListener = { index, _, _ ->
            clCost.visibility = View.VISIBLE
            tvDateCost.text = candles.toList()[index].first
            tvLineCost.text = stock.stockPrice.currency.format(candles.toList()[index].second.toDouble())
        }
    }
    @SuppressLint("SetTextI18n")
    fun initStock() {
        tvName.text = stock.symbol

        tvCompany.text = Helper.checkLengthLargeCompany(stock.company)

        tvCost.text = stock.stockPrice.currency.format(stock.stockPrice.price)
        Helper.formatChangePrice(tvChange, stock.stockPrice)
        btnBuy.text = "${getString(R.string.buy_btn)} ${stock.stockPrice.currency.format(stock.stockPrice.price)}"

        if(stock.isFavourite) ivFavourite.setImageResource(R.drawable.ic_favourite)

        getCandles(Interval.DAY)
    }
    override fun onNewsClick(news: NewsModel) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(news.url))
        startActivity(browserIntent)
    }
    companion object {
        private const val animationDuration = 1000L
    }

}