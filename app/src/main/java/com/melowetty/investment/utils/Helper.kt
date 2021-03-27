package com.melowetty.investment.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.widget.ImageView
import android.widget.TextView
import com.melowetty.investment.R
import com.melowetty.investment.activities.StockActivity
import com.melowetty.investment.database.models.FavouriteStock
import com.melowetty.investment.enums.Activities
import com.melowetty.investment.enums.Currency
import com.melowetty.investment.models.CompanyProfileModel
import com.melowetty.investment.models.FindStockModel
import com.melowetty.investment.models.Stock
import com.melowetty.investment.models.StockPrice
import com.squareup.picasso.Picasso
import okhttp3.OkHttpClient
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs


class Helper {
    companion object {
        private var httpClient: OkHttpClient? = null
        fun convertLongToTime(time: Long): String {
            val date = Date(time * 1000)
            val format = SimpleDateFormat("dd.MM.yyyy HH:mm")
            return format.format(date)
        }
        fun pasteImagefromURL(url: String, imageView: ImageView) {
            try {
                Picasso.get()
                    .load(url)
                    .into(imageView)
            }
            catch (e: Exception) {
                Picasso.get()
                    .load(R.drawable.placeholder_background)
                    .into(imageView)
            }
        }
        fun formatCost(cost: Double?): Double = DecimalFormat("#0.00").format(cost).replace(
            ',',
            '.'
        ).toDouble();
        fun spaceEveryThreeNums(num: Int): String {
            val str = num.toString()
            var output = ""
            var count = 0
            for(i in str.reversed()) {
                if(i == '-') continue
                if(count == 3) {
                    output += " "
                    count = 0
                }
                output += i
                count++

            }
            return output.reversed()
        }
        fun spaceEveryThreeNums(num: Double, symbol: Char): String {
            val output = spaceEveryThreeNums(num.toInt())
            return "$output$symbol${getNumsAfterComma(num)}"
        }
        private fun getNumsAfterComma(num: Double): String {
            val str = num.toString()
            var pos = 0
            var count = 0
            for(int in str) {
                count++
                if( int == '.' || int == ',') {
                    pos = count
                    break
                }
            }
            return str.substring(pos)
        }
        fun changeCondition(textView: TextView, active: Boolean) {
            if(active) textView.setTextAppearance(R.style.PrimaryButtonActive)
            else textView.setTextAppearance(R.style.PrimaryButtonNotActive)
        }
        private fun getUpChangeBool(percent: String): Boolean {
            return percent[0] != '-'
        }
        private fun getDoublePercentChange(percent: String): Double {
            if(getUpChangeBool(percent)) return percent.substring(0).toDouble()
            else return percent.substring(1).toDouble()
        }
        private fun companyProfileToStock(model: CompanyProfileModel, favourites: List<FavouriteStock>): Stock? {
            return try {
                val currency = Currency.getCardTypeByName(model.currency)
                val changePercent = formatCost(abs((model.changes/model.price)*100))
                val up = getUpChangeBool(model.changes.toString())
                val stockPrice = StockPrice(currency, model.price, model.changes, changePercent, up)
                val isFavourite = isFavorite(model.symbol, favourites)
                Stock(model.symbol, model.companyName, model.image, isFavourite, stockPrice)
            } catch (e: Exception) {
                null
            }
        }
        private fun isFavorite(ticker: String, favourites: List<FavouriteStock>): Boolean {
            favourites.forEach {
                if(ticker == it.ticker) return true
            }
            return false
        }
        @SuppressLint("SetTextI18n")
        fun formatChangePrice(textView: TextView, price: StockPrice) {
            val symbol = if (price.up) "+" else "-"
            val style = if (price.up) R.style.StockPriceUp else R.style.StockPriceDown
            val percent = price.changePercent.toString().replace(".", ",")
            textView.setTextAppearance(style)
            textView.text = "$symbol${price.currency.format(price.change)} ($percent%)"
        }
        fun convertModelListToStockList(array: List<CompanyProfileModel>, favourites: List<FavouriteStock>): List<Stock> {
            val stocks: ArrayList<Stock> = ArrayList()
            stocks.apply {
                array.forEach {
                    companyProfileToStock(it, favourites)?.let { it1 -> this.add(it1) }
                }
            }
            return stocks
        }
        fun convertModelListToStringList(target: FindStockModel): List<String> {
            val stocks: ArrayList<String> = ArrayList()
            stocks.apply {
                target.data.forEach {
                    this.add(it.symbol)
                }
            }
            return stocks
        }
        fun checkLengthCompany(company: String): String {
            return if (company.length > 32) company.substring(0, 32) + "..."
            else company
        }
        fun checkLengthLargeCompany(company: String): String {
            return if (company.length > 50) company.substring(0, 50) + "..."
            else company
        }
        fun getStockInfoIntent(context: Context, stock: Stock, from: Activities): Intent {
            val intent = Intent(context, StockActivity::class.java)
            intent.putExtra("stock", stock)
            intent.putExtra("from", from)
            return intent
        }
        fun setFavouriteColor(context: Context, imageView: ImageView) {
            imageView.setColorFilter(context.resources.getColor(R.color.favourite))
        }
        fun setNotFavouriteColor(context: Context, imageView: ImageView) {
            imageView.setColorFilter(context.resources.getColor(R.color.notFavourite))
        }
        fun favouriteStocksToString(stocks: List<FavouriteStock>): List<String> {
            val output: ArrayList<String> = ArrayList()
            stocks.forEach {
                output.add(it.ticker)
            }
            return output
        }
    }
}