package com.melowetty.investment.utils

import android.content.Context
import android.widget.ImageView
import com.squareup.picasso.Picasso
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class Helper {
    companion object {
        fun convertLongToTime(time: Long): String {
            val date = Date(time * 1000)
            val format = SimpleDateFormat("dd.MM.yyyy HH:mm")
            return format.format(date)
        }
        fun formatCost(cost: Float?): Float = DecimalFormat("#0.0").format(cost).replace(',','.').toFloat();
        fun pasteImage(context: Context, url: String, imageView: ImageView) {
            Picasso.with(context)
                .load(url)
                .into(imageView)
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
        fun spaceEveryThreeNums(num : Float, symbol: Char): String {
            val output = spaceEveryThreeNums(num.toInt())
            return "$output$symbol${getNumsAfterComma(num)}"
        }
        private fun getNumsAfterComma(num: Float): String {
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
    }
}