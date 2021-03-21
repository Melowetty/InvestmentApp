package com.melowetty.investment.utils

import android.widget.ImageView
import android.widget.TextView
import com.melowetty.investment.Currency
import com.melowetty.investment.R
import com.melowetty.investment.models.CompanyInfoModel
import com.melowetty.investment.models.Stock
import com.melowetty.investment.models.StockPrice
import com.squareup.picasso.Picasso
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

class Helper {
    companion object {
        fun convertLongToTime(time: Long): String {
            val date = Date(time * 1000)
            val format = SimpleDateFormat("dd.MM.yyyy HH:mm")
            return format.format(date)
        }
        fun formatCost(cost: Double?): Double = DecimalFormat("#0.00").format(cost).replace(',','.').toDouble();
        fun pasteImage(ticker: String, imageView: ImageView) {
            try {
                Picasso.get()
                    .load("https://finnhub.io/api/logo?symbol=$ticker")
                    .into(imageView)
            }
            catch (e: Exception) {
                Picasso.get()
                    .load("https://yastatic.net/s3/fintech-icons/1/i/$ticker.svg")
                    .into(imageView)
            }
        }
        fun spaceEveryThreeNums(num : Int): String {
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
        fun spaceEveryThreeNums(num : Double, symbol: Char): String {
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
        fun getUpChangeBool(percent: String): Boolean {
            return percent[0] != '-'
        }
        fun getDoublePercentChange(percent: String): Double {
            if(getUpChangeBool(percent)) return percent.substring(0, percent.length-1).toDouble()
            else return percent.substring(1, percent.length-1).toDouble()
        }
        fun companyInfoToStock(model: CompanyInfoModel): Stock {
            val result = model.quoteSummary.result.get(0).price
            val price = result.regularMarketPrice.fmt
            val priceChange = abs(result.regularMarketChange.fmt)
            val currency = Currency.getCardTypeByName(result.currency)
            val up = getUpChangeBool(result.regularMarketChangePercent.fmt)
            val priceChangePercent = getDoublePercentChange(result.regularMarketChangePercent.fmt)
            val stockPrice = StockPrice(currency, price, priceChange, priceChangePercent, up)
            return Stock(result.symbol, result.shortName, stockPrice)
        }
        fun formatChangePrice(textView: TextView, price: StockPrice) {
            val symbol = if (price.up) "+" else "-"
            val style = if (price.up) R.style.StockPriceUp else R.style.StockPriceDown
            val percent = price.changePercent.toString().replace(".",",")
            textView.setTextAppearance(style)
            textView.text = "$symbol${price.currency.format(price.change)} ($percent%)"
        }
    }
}