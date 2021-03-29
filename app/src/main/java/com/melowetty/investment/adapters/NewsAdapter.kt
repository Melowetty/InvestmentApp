package com.melowetty.investment.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.melowetty.investment.R
import com.melowetty.investment.listeners.NewsClickListener
import com.melowetty.investment.models.NewsModel
import com.melowetty.investment.utils.Helper

class NewsAdapter(
    private val news: ArrayList<NewsModel>,
    private val newsClickListener: NewsClickListener
    ) :
    RecyclerView.Adapter<NewsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.news_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = news[position]

        holder.source.text = item.site
        holder.title.text = item.title
        holder.text.text = Helper.checkLengthCompany(item.text)
        holder.date.text = item.publishedDate

        holder.itemView.setOnClickListener {
            newsClickListener.onNewsClick(item)
        }
    }

    override fun getItemCount(): Int {
        return news.size
    }

    fun addNews(news: List<NewsModel>) {
        this.news.apply {
            clear()
            addAll(news)
        }
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var source: TextView = view.findViewById<View>(R.id.news_source) as TextView
        var title: TextView = view.findViewById<View>(R.id.news_title) as TextView
        var text: TextView = view.findViewById<View>(R.id.news_text) as TextView
        var date: TextView = view.findViewById<View>(R.id.news_date) as TextView

    }
}