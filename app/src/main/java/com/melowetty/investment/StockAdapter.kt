package com.melowetty.investment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.melowetty.investment.models.Stock
import com.melowetty.investment.utils.Helper


class StockAdapter(val stocks: ArrayList<Stock>) :
    RecyclerView.Adapter<StockAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.stock_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = stocks[position]
        if (position % 2 != 0) holder.itemView.setBackgroundResource(R.drawable.stock_outline_white)
        holder.title.text = item.symbol
        holder.subtitle.text = item.company
        Helper.formatChangePrice(holder.difference, item.stockPrice)
        holder.cost.text = item.stockPrice.currency.format(item.stockPrice.price)
        Helper.pasteImage(item.symbol, holder.logo)
        holder.logo.clipToOutline = true
    }

    override fun getItemCount(): Int {
        return stocks.size
    }
    fun addStocks(stocks: List<Stock>) {
        this.stocks.apply {
            //clear()
            addAll(stocks)
        }
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var title: TextView = view.findViewById<View>(R.id.name) as TextView
        var subtitle: TextView = view.findViewById<View>(R.id.company) as TextView
        var cost: TextView = view.findViewById<View>(R.id.cost) as TextView
        var difference: TextView = view.findViewById<View>(R.id.growth) as TextView
        var logo: ImageView = view.findViewById<View>(R.id.logo) as ImageView

    }
}