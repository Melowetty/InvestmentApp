package com.melowetty.investment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.melowetty.investment.models.StockItem
import com.melowetty.investment.utils.Helper

public class StockAdapter(private val listStocks: List<StockItem>) :
    RecyclerView.Adapter<StockAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.stock_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listStocks[position]
        holder.title.text = item.title
        holder.subtitle.text = item.subtitle
        holder.difference.text = item.priceInfo.change.toString()
        holder.cost.text = item.priceInfo.price.toString()
        Helper.pasteImage(item.title, holder.logo)
        holder.logo.clipToOutline = true
    }

    override fun getItemCount(): Int {
        return listStocks.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var title: TextView
        var subtitle: TextView
        var cost: TextView
        var difference: TextView
        var logo: ImageView

        init {
            title = view.findViewById<View>(R.id.name) as TextView
            subtitle = view.findViewById<View>(R.id.company) as TextView
            cost = view.findViewById<View>(R.id.cost) as TextView
            difference = view.findViewById<View>(R.id.growth) as TextView
            logo = view.findViewById<View>(R.id.logo) as ImageView
        }
    }
}