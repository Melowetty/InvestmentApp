package com.melowetty.investment.utils

import android.widget.ImageView
import android.widget.TextView
import com.melowetty.investment.Currency
import com.melowetty.investment.R
import com.melowetty.investment.models.CompanyInfoModel
import com.melowetty.investment.models.CompanyProfileModel
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
        fun pasteImage(ticker: String, imageView: ImageView) {
            try {
                Picasso.get()
                    .load("https://financialmodelingprep.com/image-stock/$ticker.jpg")
                    .into(imageView)
            }
            catch (e: Exception) {
                Picasso.get()
                    .load("https://yastatic.net/s3/fintech-icons/1/i/$ticker.svg")
                    .into(imageView)
            }
        }
        fun pasteImagefromURL(url: String, imageView: ImageView) {
            try {
                Picasso.get()
                    .load(url)
                    .into(imageView)
            }
            catch (e: Exception) {
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
        fun companyInfoToStock(model: CompanyInfoModel): Stock? {
            return try {
                val result = model.quoteSummary.result[0].price
                val price = result.regularMarketPrice.fmt
                val priceChange = abs(result.regularMarketChange.fmt)
                val currency = Currency.getCardTypeByName(result.currency)
                val up = getUpChangeBool(result.regularMarketChangePercent.fmt)
                val priceChangePercent = getDoublePercentChange(result.regularMarketChangePercent.fmt)
                val stockPrice = StockPrice(currency, price, priceChange, priceChangePercent, up)
                Stock(result.symbol, result.shortName, "", stockPrice)
            } catch (e: Exception) {
                null
            }

        }
        fun companyProfileToStock(model: CompanyProfileModel): Stock? {
            return try {
                val currency = Currency.getCardTypeByName(model.currency)
                val changePercent = formatCost(abs((model.changes/model.price)*100))
                val up = getUpChangeBool(model.changes.toString())
                val stockPrice = StockPrice(currency, model.price, model.changes, changePercent, up)
                Stock(model.symbol, model.companyName, model.image, stockPrice)
            } catch (e: Exception) {
                null
            }
        }
        fun formatChangePrice(textView: TextView, price: StockPrice) {
            val symbol = if (price.up) "+" else "-"
            val style = if (price.up) R.style.StockPriceUp else R.style.StockPriceDown
            val percent = price.changePercent.toString().replace(".", ",")
            textView.setTextAppearance(style)
            textView.text = "$symbol${price.currency.format(price.change)} ($percent%)"
        }
        fun convertModelListToStockList(array: List<CompanyProfileModel>): List<Stock> {
            val stocks: ArrayList<Stock> = ArrayList()
            stocks.apply {
                array.forEach {
                    companyProfileToStock(it)?.let { it1 -> this.add(it1) }
                }
            }
            return stocks
        }
    }
}