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
        fun formatCost(cost: Float?): String = DecimalFormat("#0.0").format(cost);
        fun pasteImage(context: Context, url: String, imageView: ImageView) {
            Picasso.with(context)
                .load(url)
                .into(imageView)
        }
    }
}