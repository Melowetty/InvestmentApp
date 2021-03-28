package com.melowetty.investment.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.melowetty.investment.R
import com.melowetty.investment.database.models.SearchedItem
import com.melowetty.investment.listeners.ItemClickListener

class RequestsAdapter(
    private val tickers: ArrayList<SearchedItem>,
    private val itemClickListener: ItemClickListener
    ) :
    RecyclerView.Adapter<RequestsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.search_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = tickers[position]

        holder.ticker.text = item.ticker

        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(item)
        }
    }

    override fun getItemCount(): Int {
        return tickers.size
    }

    fun addTickers(tickers: List<SearchedItem>) {
        this.tickers.apply {
            clear()
            addAll(tickers)
        }
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var ticker: TextView = view.findViewById<View>(R.id.search_ticker) as TextView

    }
}