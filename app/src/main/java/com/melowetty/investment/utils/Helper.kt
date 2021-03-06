package com.melowetty.investment.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.beust.klaxon.Klaxon
import com.facebook.shimmer.ShimmerFrameLayout
import com.melowetty.investment.R
import com.melowetty.investment.database.models.FavoriteStock
import com.melowetty.investment.database.models.FoundTicker
import com.melowetty.investment.enums.Activities
import com.melowetty.investment.enums.Currency
import com.melowetty.investment.enums.Resolution
import com.melowetty.investment.models.FindStockModel
import com.melowetty.investment.models.ProfileModel
import com.melowetty.investment.models.Stock
import com.melowetty.investment.models.StockPrice
import com.melowetty.investment.ui.StockActivity
import com.squareup.picasso.Picasso
import org.threeten.bp.LocalDate
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

class Helper {
    companion object {
        const val YEAR: Long = 31536000
        const val MONTH: Long = 2629743
        const val WEEK: Long = 604800
        const val DAY: Long = 86400
        fun getUnixTime(): Long {
            val calendar = Calendar.getInstance()
            val now = calendar.timeInMillis
            return (now / 1000)
        }
        private fun convertLongToTime(time: Long, resolution: Resolution, activity: Activity): String {

            val date = Date(time * 1000)
            val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val localDate = LocalDate.parse(format.format(date))
            return when(resolution) {
                Resolution.PER_15MIN -> "${SimpleDateFormat("HH:mm", Locale.getDefault()).format(date)} ${localDate.dayOfMonth} ${getStringMonth(localDate.monthValue, activity)}"
                Resolution.PER_30MIN -> "${SimpleDateFormat("HH:mm", Locale.getDefault()).format(date)} ${localDate.dayOfMonth} ${getStringMonth(localDate.monthValue, activity)}"
                Resolution.PER_HOUR -> "${SimpleDateFormat("HH:mm", Locale.getDefault()).format(date)} ${localDate.dayOfMonth} ${getStringMonth(localDate.monthValue, activity)}"
                Resolution.PER_DAY -> "${localDate.dayOfMonth} ${getStringMonth(localDate.monthValue, activity)} ${localDate.year}"
                Resolution.PER_WEEK -> "${localDate.dayOfMonth} ${getStringMonth(localDate.monthValue, activity)} ${localDate.year}"
                Resolution.PER_MONTH -> "${getStringMonth(localDate.monthValue, activity)} ${localDate.year}"
            }
        }
        fun zipCandles(times: List<Long>, prices: List<Double>, resolution: Resolution, activity: Activity): Map<String, Float> {
            val output = mutableMapOf<String, Float>()
            for(index in times.indices) {
                val str = convertLongToTime(times[index], resolution, activity)
                output[str] = formatCost(prices[index]).toFloat()
            }
            return output
        }
        private fun getStringMonth(number: Int, activity: Activity): String {
            when(number) {
                1 -> return activity.getString(R.string.january)
                2 -> return activity.getString(R.string.february)
                3 -> return activity.getString(R.string.march)
                4 -> return activity.getString(R.string.april)
                5 -> return activity.getString(R.string.may)
                6 -> return activity.getString(R.string.june)
                7 -> return activity.getString(R.string.july)
                8 -> return activity.getString(R.string.august)
                9 -> return activity.getString(R.string.september)
                10 -> return activity.getString(R.string.october)
                11 -> return activity.getString(R.string.november)
                12 -> return activity.getString(R.string.december)
            }
            return ""
        }
        fun pasteImageFromURL(url: String, imageView: ImageView) {
            try {
                Picasso.get()
                    .load(url)
                    .error(R.drawable.placeholder_background)
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
            return if(checkInteger(num) == num.toInt().toString()) output
            else "$output$symbol${getNumsAfterComma(num)}"
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

        private fun companyProfileToStock(model: ProfileModel, favorites: List<FavoriteStock>): Stock? {
            return try {
                val currency = Currency.getCardTypeByName(model.currency)
                val changePercent = formatCost(abs((model.changes / model.price) * 100))
                val up = getUpChangeBool(model.changes.toString())
                val stockPrice = StockPrice(currency, model.price, model.changes, changePercent, up)
                val isFavourite = isFavorite(model.symbol, favorites)
                Stock(model.symbol, model.companyName, model.image, isFavourite, stockPrice)
            } catch (e: Exception) {
                null
            }
        }
        private fun isFavorite(ticker: String, favorites: List<FavoriteStock>): Boolean {
            favorites.forEach {
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
        fun convertModelListToStockList(array: List<ProfileModel>, favorites: List<FavoriteStock>): List<Stock> {
            val stocks: ArrayList<Stock> = ArrayList()
            stocks.apply {
                array.forEach {
                    companyProfileToStock(it, favorites)?.let { it1 -> this.add(it1) }
                }
            }
            return stocks
        }
        fun convertModelListToStringList(target: FindStockModel): List<String> {
            val stocks: ArrayList<String> = ArrayList()
            stocks.apply {
                target.result.forEach {
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
        fun getStockInfoIntent(context: Context, stock: Stock, cache: List<Stock>, cacheTicker:  String, from: Activities): Intent {
            val intent = Intent(context, StockActivity::class.java)
            intent.putExtra("stock", stock)
            intent.putExtra("cache", Klaxon().toJsonString(cache))
            intent.putExtra("cacheStock", cacheTicker)
            intent.putExtra("from", from)
            return intent
        }
        fun setFavouriteColor(context: Context, imageView: ImageView) {
            imageView.setColorFilter(context.resources.getColor(R.color.favourite))
        }
        fun setNotFavouriteColor(context: Context, imageView: ImageView) {
            imageView.setColorFilter(context.resources.getColor(R.color.notFavourite))
        }
        fun favouriteStocksToString(stocks: List<FavoriteStock>): List<String> {
            val output: ArrayList<String> = ArrayList()
            stocks.forEach {
                output.add(it.ticker)
            }
            return output
        }
        fun convertStringListToSearchedItem(target: List<String>): ArrayList<FoundTicker> {
            val output = arrayListOf<FoundTicker>()
            target.forEach {
                output.add(FoundTicker(it))
            }
            return output
        }
        fun transferToActivity(activity: Activity, cls: Class<*>?) {
            val intent = Intent(activity, cls)
            activity.startActivity(intent)
        }
        fun stopShimmer(sfl: ShimmerFrameLayout) {
            sfl.stopShimmer()
            sfl.visibility = View.GONE
        }
        fun startShimmer(sfl: ShimmerFrameLayout) {
            sfl.visibility = View.VISIBLE
            sfl.startShimmer()
        }
        fun checkInteger(cost: Double): String {
            return if((cost - cost.toInt()) == 0.0) cost.toInt().toString()
            else cost.toString()
        }
    }
}