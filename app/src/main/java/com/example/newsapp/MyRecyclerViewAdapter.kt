package com.example.newsapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class MyRecyclerViewAdapter(
    private val itemList: List<ListItem>,
    private val context: Context
    ) : RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder>() {


    var filteredNewsList = ArrayList(itemList)

    fun filter(query: String) {
        filteredNewsList.clear() // Clear previous results
        if (query.isEmpty()) {
            filteredNewsList.addAll(itemList) // If empty, show all items
        } else {
            filteredNewsList.addAll(itemList.filter {
                it.title.contains(query, ignoreCase = true)
            })
        }
        notifyDataSetChanged() // Notify RecyclerView of changes
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.itemTitle)
        val subtitleTextView: TextView = itemView.findViewById(R.id.itemSubtitle)
        val timestampTextView: TextView = itemView.findViewById(R.id.itemTimestamp)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.titleTextView.text = item.title
        holder.subtitleTextView.text = item.subtitle
        holder.timestampTextView.text = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            .format(Date(item.timestamp))

        holder.titleTextView.setOnClickListener {
            val intent = Intent(context, DetailNewsScreenActivity::class.java)
            intent.putExtra("title", item.title)
            //intent.putExtra("subtitle", item.subtitle)
            intent.putExtra("timestamp", item.timestamp)
            context.startActivity(intent)

        }
    }

    override fun getItemCount(): Int = itemList.size

}
