package com.example.newsapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SearchableNewsItemAdapter(private val newsList: MutableList<ListItem>) :
    RecyclerView.Adapter<SearchableNewsItemAdapter.NewsItemViewHolder>() {

    var filteredNewsList = ArrayList(newsList)

    fun filter(query: String) {
        filteredNewsList.clear() // Clear previous results
        if (query.isEmpty()) {
            filteredNewsList.addAll(newsList) // If empty, show all items
        } else {
            filteredNewsList.addAll(newsList.filter {
                it.title.contains(query, ignoreCase = true)
            })
        }
        notifyDataSetChanged() // Notify RecyclerView of changes
    }

    class NewsItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.itemTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return NewsItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsItemViewHolder, position: Int) {
        val newsItem = filteredNewsList[position]
        holder.titleTextView.text = newsItem.title
    }

    override fun getItemCount(): Int = filteredNewsList.size
}