package com.melowetty.investment.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.melowetty.investment.R
import com.melowetty.investment.listeners.StockClickListener
import com.melowetty.investment.models.Stock
import com.melowetty.investment.utils.Helper


class StocksAdapter(
    val stocks: ArrayList<Stock>,
    private val stockClickListener: StockClickListener
) :
    RecyclerView.Adapter<StocksAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.stock_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = stocks[position]

        if (position % 2 != 0)
            holder.itemView.setBackgroundResource(R.drawable.stock_outline_white)
        else
            holder.itemView.setBackgroundResource(R.drawable.stock_outline)

        holder.title.text = item.symbol
        holder.subtitle.text = Helper.checkLengthCompany(item.company)

        holder.cost.text = item.stockPrice.currency.format(item.stockPrice.price)

        Helper.formatChangePrice(holder.difference, item.stockPrice)

        if(item.isFavourite)
            Helper.setFavouriteColor(holder.itemView.context, holder.favourite)
        else
            Helper.setNotFavouriteColor(holder.itemView.context, holder.favourite)

        holder.itemView.setOnClickListener {
            stockClickListener.onStockClick(item)
        }

        Helper.pasteImagefromURL(item.logo, holder.logo)
        holder.logo.clipToOutline = true
    }

    override fun getItemCount(): Int {
        return stocks.size
    }
    fun addStocks(stocks: List<Stock>) {
        this.stocks.apply {
            clear()
            addAll(stocks)
        }
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var title: TextView = view.findViewById<View>(R.id.name) as TextView
        var subtitle: TextView = view.findViewById<View>(R.id.company) as TextView
        var cost: TextView = view.findViewById<View>(R.id.cost) as TextView
        var difference: TextView = view.findViewById<View>(R.id.growth) as TextView
        var logo: ImageView = view.findViewById<View>(R.id.logo) as ImageView
        var favourite: ImageView = view.findViewById<View>(R.id.favourite_icon) as ImageView
    }
}