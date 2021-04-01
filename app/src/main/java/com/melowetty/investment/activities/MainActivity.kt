package com.melowetty.investment.activities

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.melowetty.investment.AppActivity
import com.melowetty.investment.R
import com.melowetty.investment.adapters.StocksAdapter
import com.melowetty.investment.database.AppDatabase
import com.melowetty.investment.database.models.FavoriteStock
import com.melowetty.investment.enums.Activities
import com.melowetty.investment.enums.Indices
import com.melowetty.investment.listeners.StockClickListener
import com.melowetty.investment.models.Stock
import com.melowetty.investment.utils.Helper
import com.melowetty.investment.viewmodels.FavoriteStocksViewModel
import com.melowetty.investment.viewmodels.IndicesConstituentsViewModel
import com.melowetty.investment.viewmodels.ProfileViewModel


class MainActivity : AppCompatActivity(), StockClickListener {

    private lateinit var tvFavourite: TextView
    private lateinit var tvStocks: TextView
    private lateinit var tvError: TextView
    private lateinit var tvSearchBar: TextView
    private lateinit var tvNotFoundFavorites: TextView
    private lateinit var rv: RecyclerView
    private lateinit var sfl: ShimmerFrameLayout

    private lateinit var adapter: StocksAdapter
    private lateinit var indicesConstituentsModel: IndicesConstituentsViewModel
    private lateinit var companyProfileModel: ProfileViewModel
    private lateinit var favoriteStocksViewModel: FavoriteStocksViewModel

    private var favoriteStocks: List<FavoriteStock> = ArrayList()
    private var stocks: List<Stock> = ArrayList()
    private var db: AppDatabase? = null
    private var target: Activities? = null
    private val index = Indices.NASDAQ_100

    private var isShowError = false
    private var isFavourite = false

    private var waitingFavorites = false
    private var initFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rv = findViewById(R.id.recyclerView)

        rv.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter = StocksAdapter(arrayListOf(), this)
        rv.adapter = adapter


        tvFavourite = findViewById(R.id.favourite)
        tvStocks = findViewById(R.id.stocks)
        tvSearchBar = findViewById(R.id.search_bar)
        sfl = findViewById(R.id.shimmer_view_container)
        tvError = findViewById(R.id.main_error)
        tvNotFoundFavorites = findViewById(R.id.not_found_favorites)

        target = intent.getSerializableExtra("target") as? Activities

        tvFavourite.setOnClickListener {
            hideNotFoundFavoritesMessage()
            Helper.changeCondition(tvFavourite, true)
            Helper.changeCondition(tvStocks, false)
            Helper.startShimmer(sfl)
            rv.visibility = View.GONE
            isFavourite = true
            getFavouriteCompanyProfile(Helper.favouriteStocksToString(favoriteStocks))
        }
        tvStocks.setOnClickListener {
            hideNotFoundFavoritesMessage()
            Helper.changeCondition(tvFavourite, false)
            Helper.changeCondition(tvStocks, true)
            Helper.startShimmer(sfl)
            rv.visibility = View.GONE
            getIndexConstituents(index)
            isFavourite = false
        }
        tvSearchBar.setOnClickListener {
            Helper.transferToActivity(this, SearchActivity::class.java)
        }

        initDatabase()
        initModels()
        initObservers()

        sfl.startShimmer();

        if(target != null) {
            if(target == Activities.FAVOURITE) {
                Helper.changeCondition(tvFavourite, true)
                Helper.changeCondition(tvStocks, false)
                getFavouriteCompanyProfile(Helper.favouriteStocksToString(favoriteStocks))
                rv.visibility = View.GONE
                isFavourite = true
            }
            if(target == Activities.MAIN)
                getIndexConstituents(index)
        }
        else getIndexConstituents(index)

    }
    private fun initDatabase() {
        db = AppActivity.getDatabase()
    }
    private fun initModels() {
        companyProfileModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)

        indicesConstituentsModel =
            ViewModelProvider(this).get(IndicesConstituentsViewModel::class.java)

        favoriteStocksViewModel =
            ViewModelProvider(this).get(FavoriteStocksViewModel::class.java)
    }
    private fun initObservers() {
        favoriteStocksViewModel.favoriteStocks.observe(this) {
            it?.let {
                favoriteStocks = it
                initFavorite = true
                if (waitingFavorites && it.isNotEmpty()) {
                    getFavouriteCompanyProfile(Helper.favouriteStocksToString(favoriteStocks))
                } else if(waitingFavorites && it.isEmpty())
                    showNotFoundFavoritesMessage()
                waitingFavorites = false
            }
        }

        indicesConstituentsModel
            .getConstituentsObserver()
            .observe(this) {
                if (it != null) {
                    if (isShowError) hideErrorMessage()
                    getCompanyProfile(it.constituents.joinToString(separator = ","))
                } else {
                    showErrorMessage()
                }
            }

        companyProfileModel
            .getCompanyProfileObserver()
            .observe(this) {
                if (it != null) {
                    if (isShowError) hideErrorMessage()
                    retrieveList(
                        Helper.convertModelListToStockList(
                            it,
                            favoriteStocks))
                } else {
                    showErrorMessage()
                }
            }
    }
    private fun showNotFoundFavoritesMessage() {
        tvNotFoundFavorites.visibility = View.VISIBLE
        rv.visibility = View.GONE
        sfl.stopShimmer()
        sfl.visibility = View.GONE
    }
    private fun hideNotFoundFavoritesMessage() {
        tvNotFoundFavorites.visibility = View.GONE
    }
    private fun showErrorMessage() {
        isShowError = true
        tvError.visibility = View.VISIBLE
        rv.visibility = View.GONE
        sfl.stopShimmer()
        sfl.visibility = View.GONE
    }
    private fun hideErrorMessage() {
        isShowError = false
        tvError.visibility = View.GONE
    }
    private fun getFavouriteCompanyProfile(favourites: List<String>) {
        if(initFavorite)
            showNotFoundFavoritesMessage()
        else if(favoriteStocks.isNullOrEmpty() && !initFavorite)
            waitingFavorites = true
        else getCompanyProfile(favourites.joinToString(","))
    }
    private fun getIndexConstituents(indice: Indices) {
        indicesConstituentsModel.makeApiCall(indice.code)
    }
    private fun getCompanyProfile(ticker: String) {
        companyProfileModel.makeApiCall(ticker)
    }
    private fun retrieveList(stocks: List<Stock>) {
        this.stocks = stocks
        adapter.apply {
            this.addStocks(stocks)
            sfl.stopShimmer()
            sfl.visibility = View.GONE
            rv.visibility = View.VISIBLE
            notifyDataSetChanged()
        }
    }

    override fun onStockClick(stock: Stock) {
        if(isFavourite) startActivity(
            Helper.getStockInfoIntent(this, stock, Activities.FAVOURITE))
        else startActivity(
            Helper.getStockInfoIntent(this, stock, Activities.MAIN))
    }
}
